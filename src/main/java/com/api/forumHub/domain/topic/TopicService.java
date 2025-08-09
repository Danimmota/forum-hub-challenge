package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.Answer;
import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.answer.AnswerMapper;
import com.api.forumHub.domain.answer.AnswerService;
import com.api.forumHub.domain.course.Course;
import com.api.forumHub.domain.course.CourseRepository;
import com.api.forumHub.domain.user.Role;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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

    public TopicService(TopicRepository topicRepository, UserRepository userRepository, CourseRepository courseRepository, AnswerService answerService) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.answerService = answerService;
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

    public AnswerDTO replyTopic(Long topicId, AnswerDTO answerDTO, String authenticatedUserEmail) {

        Topic topic = findTopic(topicId);

        if(topic.isClosed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The topic is already marked as resolved.");
        }

        User user = authenticatedUser(authenticatedUserEmail);

        Answer answer = new Answer();
        answer.setMessage(answerDTO.message());
        answer.setCreationDate(LocalDateTime.now());
        answer.setAuthor(user);
        answer.setTopic(topic);

        AnswerDTO newResponse = AnswerMapper.toDto(answer);

        answerService.createAnswer(newResponse, authenticatedUserEmail);

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
    public TopicResponseDTO updateTopic (Long id, TopicUpdateRequest request, String authenticatedUserEmail) {

        Topic newTopic = findTopic(id);

        User user = authenticatedUser(authenticatedUserEmail);

        if(!newTopic.getAuthor().getEmail().equals(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User without permission to edit this topic!");
        }

        if(newTopic.isClosed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The topic is already marked as resolved.");
        }

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

    public void deleteTopic(Long id, String authenticatedUserEmail) {

        Topic topic = findTopic(id);

        User user = authenticatedUser(authenticatedUserEmail);

        boolean isAuthor = topic.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ROLE_ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("User not have permission to delete this topic");
        }
        topicRepository.delete(topic);
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
