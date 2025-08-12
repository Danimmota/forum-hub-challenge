package com.api.forumHub.domain.topic.validation;

import com.api.forumHub.domain.topic.Topic;
import com.api.forumHub.domain.topic.TopicRepository;
import com.api.forumHub.domain.user.Role;
import com.api.forumHub.domain.user.User;
import com.api.forumHub.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class ValidationDeleteOrAlterTopic implements TopicValidator {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public ValidationDeleteOrAlterTopic(TopicRepository topicRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;

    }

    @Override //Somente o autor e administrador podem deletar o tópico
    public void validate(Long topicId) {

        String authenticatedUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findEntityByEmail(authenticatedUserEmail);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthenticated user.");
        }

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        boolean isAuthor = topic.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().equals(Role.ROLE_ADMIN);

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("User does not have permission to perform this action on this topic");
        }
    }

}
