package com.api.forumHub.domain.answer;

import com.api.forumHub.domain.topic.Topic;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserMapper;

public class AnswerMapper {

    public static Answer toEntity(AnswerDTO dto) {
        Answer answer = new Answer();
        answer.setMessage(dto.message());
        answer.setCreationDate(dto.creationDate());

        User author = UserMapper.toUserDtoEntity(dto.author());
        answer.setAuthor(author);

        Topic topic = new Topic();
        topic.setId(dto.topic());

        answer.setTopic(topic);

        return answer;
    }

    public static AnswerDTO toDto(Answer answer) {
        return new AnswerDTO(
                answer.getId(),
                answer.getMessage(),
                answer.getCreationDate(),
                UserMapper.toDto(answer.getAuthor()),
                answer.getTopic().getId()
        );
    }

}

