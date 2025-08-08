package com.api.forumHub.domain.topic;

import jakarta.validation.constraints.NotBlank;

public record TopicUpdateRequest(
        @NotBlank(message = "O título é obrigatório") String title,
        @NotBlank(message = "A mensagem é obrigatória") String message,
        TopicStatus status) {
}
