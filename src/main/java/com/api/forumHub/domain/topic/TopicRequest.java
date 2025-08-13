package com.api.forumHub.domain.topic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicRequest(
        @NotBlank(message = "Title is mandatory.") String title,
        @NotBlank(message = "The message field is mandatory.") String message,
        @NotNull Long course
) {
}
