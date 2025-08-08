package com.api.forumHub.domain.user;

import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.course.CourseMapper;
import com.api.forumHub.domain.topic.TopicResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public static User toEntity(UserRequest request, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);

        return user;
    }

    public static User toUserDtoEntity(UserResponseDTO dto) {
        return new User(dto.id(), dto.name(), dto.email(), null, dto.role(), null, null);
    }

    public static UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static UserDetailResponse toDetailDto(User user) {
        List<AnswerDTO> answerDTOs = user.getAnswers().stream().map(a ->
                new AnswerDTO(a.getId(), a.getMessage(), a.getCreationDate(), toDto(a.getAuthor()), a.getTopic().getId())).toList();
        List<TopicResponseDTO> topicDTOs = user.getTopics().stream().map( t ->
                new TopicResponseDTO(t.getId(), t.getTitle(), t.getMessage(), t.getCreationDate(), t.getStatus(), CourseMapper.toDto(t.getCourse()), toDto(t.getAuthor()), answerDTOs)).toList();
        return new UserDetailResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                topicDTOs,
                answerDTOs
        );
    }

}
