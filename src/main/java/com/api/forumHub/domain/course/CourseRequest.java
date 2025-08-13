package com.api.forumHub.domain.course;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank(message = "Course name is required.") String name) {
}
