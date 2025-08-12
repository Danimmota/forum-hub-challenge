package com.api.forumHub.domain.answer;

import com.api.forumHub.domain.topic.Topic;
import com.api.forumHub.domain.topic.TopicRepository;
import com.api.forumHub.domain.user.Role;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public AnswerService(AnswerRepository answerRepository, TopicRepository topicRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public AnswerResponseDTO createAnswer(AnswerRequest request, String authenticatedUserEmail) {

        Topic topic = topicRepository.findById(request.topic())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found!"));

        User user = authenticatedUser(authenticatedUserEmail);

        Answer answer = new Answer();
        answer.setMessage(request.message());
        answer.setCreationDate(LocalDateTime.now());
        answer.setAuthor(user);
        answer.setTopic(topic);

        answerRepository.save(answer);

        return AnswerMapper.toDto(answer);
    }

    public List<AnswerResponseDTO> listAnswersByTopicOrderByDate(Long topicId) {
        List<Answer> answers = answerRepository.findByTopicIdOrderByCreationDateDesc(topicId);
        return answers.stream().map(AnswerMapper::toDto).toList();
    }

    public List<AnswerResponseDTO> listAnswers() {
        return answerRepository.findAll().stream()
                .map(AnswerMapper::toDto)
                .toList();
    }

    public List<AnswerResponseDTO> listAnswersByAuthor(Long authorId) {
        List<Answer> answers = answerRepository.findAnswerByAuthor(authorId);
        return answers.stream().map(AnswerMapper::toDto).toList();
    }

    public void deleteAnswer(Long answerId, String authenticatedUserEmail) {

        User user = authenticatedUser(authenticatedUserEmail);

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found by id: " + answerId));

        boolean isAuthor = answer.getAuthor().getId().equals(user.getId());
        boolean isAdmin = user.getRole().equals(Role.ROLE_ADMIN);

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("User does not have permission to perform this action on this answer");
        }

        answerRepository.deleteById(answerId);
    }

    private User authenticatedUser(String authenticatedUserEmail) {
        User user = userRepository.findEntityByEmail(authenticatedUserEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthenticated user.");
        }
        return user;
    }
}
