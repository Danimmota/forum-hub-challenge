package com.api.forumHub.domain.course;

import com.api.forumHub.domain.answer.AnswerMapper;
import com.api.forumHub.domain.answer.AnswerResponseDTO;
import com.api.forumHub.domain.topic.TopicResponseDTO;
import com.api.forumHub.domain.user.UserMapper;

import java.util.List;

public class CourseMapper {

    public static CourseDTO toDto(Course course) {
        List<TopicResponseDTO> topicDTOs = course.getTopics().stream().map(topic -> {

            List<AnswerResponseDTO> answerDTOs = topic.getAnswers().stream()
                    .map(AnswerMapper::toDto)
                    .toList();

            return new TopicResponseDTO(
                    topic.getId(),
                    topic.getTitle(),
                    topic.getMessage(),
                    topic.getCreationDate(),
                    topic.getStatus(),
                    topic.getCourse().getId(),
                    UserMapper.toDto(topic.getAuthor()),
                    answerDTOs
            );
        }).toList();

            return new CourseDTO(course.getId(), course.getName(), topicDTOs);
    }
}
