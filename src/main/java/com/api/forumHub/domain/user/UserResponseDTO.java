package com.api.forumHub.domain.user;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role) {
}
