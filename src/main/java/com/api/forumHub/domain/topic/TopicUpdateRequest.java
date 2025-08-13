package com.api.forumHub.domain.topic;

import jakarta.validation.constraints.NotBlank;

public record TopicUpdateRequest(
        @NotBlank(message = "Title is mandatory.") String title,
        @NotBlank(message = "The message field is mandatory.") String message
        ) {}
