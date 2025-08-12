package com.api.forumHub.domain.answer;

import com.api.forumHub.domain.user.UserResponseDTO;

import java.time.LocalDateTime;

public record AnswerResponseDTO(
        Long id,
        String message,
        LocalDateTime creationDate,
        UserResponseDTO author,
        Long topic) {
}
