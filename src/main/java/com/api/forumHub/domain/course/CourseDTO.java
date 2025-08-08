package com.api.forumHub.domain.course;

import com.api.forumHub.domain.topic.TopicResponseDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CourseDTO(
        Long id,
        @NotBlank(message = "Course name is required.") String name,
        List<TopicResponseDTO> topics) {
}
