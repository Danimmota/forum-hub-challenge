package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.course.CourseDTO;
import com.api.forumHub.domain.user.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record TopicResponseDTO(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        TopicStatus status,
        CourseDTO course,
        UserResponseDTO author,
        List<AnswerDTO> answers
        ) {


//    public static Page<TopicDTO> convert(Page<Topic> topics) {return topics.map(TopicDTO::new);}
}

