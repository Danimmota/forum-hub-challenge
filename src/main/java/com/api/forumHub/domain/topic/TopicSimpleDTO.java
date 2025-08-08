package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.course.CourseDTO;
import com.api.forumHub.domain.user.UserResponseDTO;

import java.time.LocalDateTime;

public record TopicSimpleDTO(
        Long id,
        String title,
        String message,
        LocalDateTime creationDate,
        TopicStatus status,
        CourseDTO course,
        UserResponseDTO author
) {
}
