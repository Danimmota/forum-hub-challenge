package com.api.forumHub.domain.answer;

import com.api.forumHub.domain.user.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AnswerDTO (
        Long id,
        @NotBlank(message = "Mensagem é obrigatória") String message,
        LocalDateTime creationDate,
        @NotNull UserResponseDTO author,
        @NotNull Long topic) {
}
