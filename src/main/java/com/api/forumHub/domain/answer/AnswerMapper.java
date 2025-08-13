package com.api.forumHub.domain.answer;

import com.api.forumHub.domain.topic.Topic;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserMapper;


public class AnswerMapper {

    public static AnswerResponseDTO toDto(Answer answer) {

        return new AnswerResponseDTO(
                answer.getId(),
                answer.getMessage(),
                answer.getCreationDate(),
                UserMapper.toDto(answer.getAuthor()),
                answer.getTopic().getId()
        );
    }

    public static Answer fromResponseDto(AnswerResponseDTO dto) {
        Answer answer = new Answer();

        answer.setId(dto.id());
        answer.setMessage(dto.message());
        answer.setCreationDate(dto.creationDate());

        User author = UserMapper.toUserDtoEntity(dto.author());
        answer.setAuthor(author);

        Topic topic = new Topic();
        topic.setId(dto.topic());
        answer.setTopic(topic);

        return answer;
    }

}

