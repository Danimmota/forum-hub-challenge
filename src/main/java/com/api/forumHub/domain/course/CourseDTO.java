package com.api.forumHub.domain.course;

import com.api.forumHub.domain.topic.TopicResponseDTO;

import java.util.List;

public record CourseDTO(
        Long id,
        String name,
        List<TopicResponseDTO> topics) {
}
