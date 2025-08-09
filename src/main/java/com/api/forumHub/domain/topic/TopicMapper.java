package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.AnswerMapper;
import com.api.forumHub.domain.course.Course;
import com.api.forumHub.domain.course.CourseMapper;
import com.api.forumHub.domain.user.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {

    public static Topic toEntity(TopicRequest request) {
        Topic topic = new Topic();
        topic.setTitle(request.title());
        topic.setMessage(request.message());
        topic.setStatus(request.status());

        Course course = new Course();
        course.setId(request.course());

        topic.setAuthor(request.author());

        return topic;
    }

    public static TopicResponseDTO toResponseDto(Topic topic) {
        return new TopicResponseDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreationDate(),
                topic.getStatus(),
                CourseMapper.toDto(topic.getCourse()),
                UserMapper.toDto(topic.getAuthor()),
                topic.getAnswers().stream().map(AnswerMapper::toDto).toList());
    }

}