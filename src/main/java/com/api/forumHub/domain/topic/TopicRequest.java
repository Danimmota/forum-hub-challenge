package com.api.forumHub.domain.topic;

import com.api.forumHub.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicRequest(
        @NotBlank(message = "O título é obrigatório") String title,
        @NotBlank(message = "O campo mensagem é de preenchimento obrigatório.") String message,
        TopicStatus status,
        @NotNull Long course,
        @NotNull User author
) {
}
