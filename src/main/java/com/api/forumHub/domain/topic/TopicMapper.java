package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.AnswerMapper;
import com.api.forumHub.domain.user.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {

    public static TopicResponseDTO toResponseDto(Topic topic) {
        return new TopicResponseDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreationDate(),
                topic.getStatus(),
                topic.getCourse().getId(),
                UserMapper.toDto(topic.getAuthor()),
                topic.getAnswers().stream().map(AnswerMapper::toDto).toList());
    }
}