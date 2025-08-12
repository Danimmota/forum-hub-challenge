package com.api.forumHub.domain.answer;

import com.api.forumHub.domain.user.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
        @NotBlank(message = "Mensagem é obrigatória") String message,
        @NotNull UserResponseDTO author,
        @NotNull Long topic) {
}
