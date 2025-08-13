package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.AnswerMapper;
import com.api.forumHub.domain.course.Course;
import com.api.forumHub.domain.course.CourseDTO;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

    public static Object toEntity(TopicRequest request) {
        Topic topic = new Topic();
        topic.setTitle(request.title());
        topic.setMessage(request.message());
        topic.setCreationDate(LocalDateTime.now());
        topic.setStatus(TopicStatus.PENDENTE);

        return topic;
    }
}