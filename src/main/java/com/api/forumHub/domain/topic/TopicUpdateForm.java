package com.api.forumHub.domain.topic;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TopicUpdateForm (
        @NotNull
        Long id,
        String title,
        String message,
        LocalDateTime changeDate
) {
}
