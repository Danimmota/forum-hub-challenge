package com.api.forumHub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank String name,
        @NotBlank @Email(message = "Formato do email é inválido") String email,
        @NotBlank @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?~-]).{8,}$",
                message = "Formato de senha inválido. A senha deve conter ao menos 8 caracteres, 1 letra maiúscula, 1 número e 1 caractere especial."
        )
        String password) {

}
