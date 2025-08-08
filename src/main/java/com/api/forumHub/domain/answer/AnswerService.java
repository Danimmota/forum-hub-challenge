package com.api.forumHub.domain.answer;

import com.api.forumHub.domain.topic.Topic;
import com.api.forumHub.domain.topic.TopicRepository;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
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

    public AnswerDTO createAnswer(AnswerDTO dto, String authenticatedUserEmail) {
        Topic topic = topicRepository.findById(dto.topic())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found!"));

        User user = userRepository.findEntityByEmail(authenticatedUserEmail);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthenticated user.");
        }

        Answer answer = new Answer();
        answer.setMessage(dto.message());
        answer.setCreationDate(LocalDateTime.now());
        answer.setAuthor(user);
        answer.setTopic(topic);

        answerRepository.save(answer);

        return AnswerMapper.toDto(answer);
    }

    public List<AnswerDTO> listAnswersByTopicOrderByDate(Long topicId) {
        List<Answer> answers = answerRepository.findByTopicIdOrderByCreationDateDesc(topicId);
        return answers.stream().map(AnswerMapper::toDto).toList();
    }

    public List<AnswerDTO> listAnswers() {
        return answerRepository.findAll().stream()
                .map(AnswerMapper::toDto)
                .toList();
    }

    public List<AnswerDTO> listAnswersByAuthor(Long authorId) {
        List<Answer> answers = answerRepository.findAnswerByAuthor(authorId);
        return answers.stream().map(AnswerMapper::toDto).toList();
    }
}
