package com.api.forumHub.domain.answer;

import jakarta.validation.constraints.NotBlank;

public record AnswerRequest(
        @NotBlank(message = "he message field is mandatory.") String message){
}
