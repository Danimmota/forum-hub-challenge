package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.answer.AnswerMapper;
import com.api.forumHub.domain.course.Course;
import com.api.forumHub.domain.course.CourseMapper;
import com.api.forumHub.domain.user.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public static Topic toSimpleDtoEntity(TopicSimpleDTO dto) {
        Topic topic = new Topic();
        topic.setTitle(dto.title());
        topic.setMessage(dto.message());
        topic.setCreationDate(dto.creationDate());
        topic.setStatus(dto.status());
        topic.setCourse(CourseMapper.toEntity(dto.course()));
        topic.setAuthor(UserMapper.toUserDtoEntity(dto.author()));

        return topic;
    }

    public static TopicSimpleDTO toSimpleDto(Topic topic) {
        return new TopicSimpleDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getCreationDate(),
                topic.getStatus(),
                CourseMapper.toDto(topic.getCourse()),
                UserMapper.toDto(topic.getAuthor())
        );
    }

}