package com.api.forumHub.domain.user;

import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.topic.TopicResponseDTO;

import java.util.List;

public record UserDetailResponse(
        Long id,
        String name,
        String email,
        Role role,
        List<TopicResponseDTO> topics,
        List<AnswerDTO> answers
) {
}
