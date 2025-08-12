package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.*;
import com.api.forumHub.domain.course.Course;
import com.api.forumHub.domain.course.CourseRepository;
import com.api.forumHub.domain.topic.validation.TopicValidator;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final AnswerService answerService;

    private final List<TopicValidator> validators;

    public TopicService(TopicRepository topicRepository, UserRepository userRepository, CourseRepository courseRepository, AnswerService answerService, List<TopicValidator> validadores) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.answerService = answerService;
        this.validators = validadores;
    }


    public TopicResponseDTO createTopic (TopicRequest request, String authenticatedUserEmail) {

        Course course = courseRepository.findById(request.course())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));

        User user = authenticatedUser(authenticatedUserEmail);

        Topic newTopic = new Topic();
        newTopic.setTitle( request.title());
        newTopic.setMessage(request.message());
        newTopic.setCreationDate(LocalDateTime.now());
        newTopic.setStatus(TopicStatus.PENDENTE);
        newTopic.setCourse(course);
        newTopic.setAuthor(user);

        topicRepository.save(newTopic);

        return TopicMapper.toResponseDto(newTopic);
    }

    public AnswerResponseDTO replyTopic(Long topicId, AnswerRequest request, String authenticatedUserEmail) {

        Topic topic = findTopic(topicId);

        validators.forEach(v -> v.validate(topicId));

        User user = authenticatedUser(authenticatedUserEmail);

        Answer answer = new Answer();
        answer.setMessage(request.message());
//        answer.setCreationDate(LocalDateTime.now());
        answer.setAuthor(user);
        answer.setTopic(topic);

        AnswerResponseDTO newResponse = AnswerMapper.toDto(answer);

        answerService.createAnswer(request, authenticatedUserEmail);

        return newResponse;
    }

    @Transactional
    public TopicResponseDTO updateStatusTopic (Long topicId, String authenticatedUserEmail) {

        Topic newTopic = findTopic(topicId);

        User user = authenticatedUser(authenticatedUserEmail);

        if(!newTopic.getAuthor().getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User without permission to edit this topic!");
        }

        newTopic.setStatus(TopicStatus.SOLUCIONADO);

        return TopicMapper.toResponseDto(newTopic);
    }

    @Transactional
    public TopicResponseDTO updateTopic (Long topicId, TopicUpdateRequest request) {

        Topic newTopic = findTopic(topicId);

        validators.forEach(v -> v.validate(topicId));

        newTopic.setTitle(request.title());
        newTopic.setMessage(request.message());

        return TopicMapper.toResponseDto(newTopic);
    }

    public TopicResponseDTO getTopicResponse(Long id) {
        Topic newTopic = findTopic(id);
        return TopicMapper.toResponseDto(newTopic);
    }

    public Page<TopicResponseDTO> getListTopics(Pageable pagination) {
        return topicRepository.findAll(pagination)
                .map(TopicMapper::toResponseDto);
    }

    public Page<TopicResponseDTO> getTopicsByCourseName(String courseName, Pageable pagination){
        return topicRepository.existsByCourseName(courseName, pagination)
                .map(TopicMapper::toResponseDto);
    }

    public Page<TopicResponseDTO> getTopicsByAuthor(Long authorId, Pageable pagination) {
        return topicRepository.existsByAuthorId(authorId, pagination)
                .map(TopicMapper::toResponseDto);
    }

    public List<TopicResponseDTO> getTopicsByTermTitle(String termInTheTitle) {
        List<Topic> topics = topicRepository.topicByTermInTheTitle(termInTheTitle);

        if(topics.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No topics found with the term entered.");
        }

        return topics.stream()
                .map(TopicMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteTopic(Long topicId) {

        validators.forEach(v -> v.validate(topicId));

        topicRepository.deleteById(topicId);
    }

    private Topic findTopic(Long id) {
         return topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found!"));
    }

    private User authenticatedUser(String authenticatedUserEmail){
        User user = userRepository.findEntityByEmail(authenticatedUserEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthenticated user.");
        }

        return user;
    }

}
