package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.*;
import com.api.forumHub.domain.course.Course;
import com.api.forumHub.domain.course.CourseRepository;
import com.api.forumHub.domain.topic.validation.TopicOperationType;
import com.api.forumHub.domain.topic.validation.TopicValidator;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    public TopicService(TopicRepository topicRepository, UserRepository userRepository, CourseRepository courseRepository, AnswerService answerService, List<TopicValidator> validators) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.answerService = answerService;
        this.validators = validators;
    }

    @Transactional
    public TopicResponseDTO createTopic (@Valid TopicRequest request, String authenticatedUserEmail) {

        User author = authenticatedUser(authenticatedUserEmail);

        Course course = courseRepository.findById(request.course())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found!"));

        Topic newTopic = new Topic();
        newTopic.setTitle(request.title());
        newTopic.setMessage(request.message());
        newTopic.setCreationDate(LocalDateTime.now());
        newTopic.setStatus(TopicStatus.PENDENTE);
        newTopic.setCourse(course);
        newTopic.setAuthor(author);

        Topic saved = topicRepository.save(newTopic);

        return TopicMapper.toResponseDto(saved);
    }

    @Transactional
    public AnswerResponseDTO replyTopic(Long topicId, AnswerRequest request, String authenticatedUserEmail) {

        runValidators(topicId, TopicOperationType.REPLY);

        Topic topic = findTopic(topicId);

        User author = authenticatedUser(authenticatedUserEmail);

        return answerService.createAnswer(request, author, topic);
    }

    @Transactional
    public TopicResponseDTO updateStatusTopic (Long topicId, TopicUpdateStatusRequest request) {

        runValidators(topicId, TopicOperationType.UPDATE_STATUS);

        Topic newTopic = findTopic(topicId);

        newTopic.setStatus(TopicStatus.SOLUCIONADO);

        return TopicMapper.toResponseDto(newTopic);
    }

    @Transactional
    public TopicResponseDTO updateTopic (Long topicId, TopicUpdateRequest request) {

        runValidators(topicId, TopicOperationType.UPDATE);

        Topic newTopic = findTopic(topicId);

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
        return topicRepository.findByCourseNameIgnoreCase(courseName, pagination)
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

        runValidators(topicId, TopicOperationType.DELETE);

        topicRepository.deleteById(topicId);
    }

    private Topic findTopic(Long id) {
         return topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found!"));
    }

    private User authenticatedUser(String authenticatedUserEmail){

        return userRepository.findUserByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthenticated user."));
    }

    private void runValidators(Long topicId, TopicOperationType operationType) {
        validators.stream()
                .filter(v -> v.getOperationType().contains(operationType))
                .forEach(v -> v.validate(topicId));
    }

}
