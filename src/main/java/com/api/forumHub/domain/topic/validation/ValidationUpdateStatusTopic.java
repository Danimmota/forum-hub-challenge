package com.api.forumHub.domain.topic.validation;

import com.api.forumHub.domain.topic.Topic;
import com.api.forumHub.domain.topic.TopicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class ValidationUpdateStatusTopic implements TopicValidator {

    private final TopicRepository topicRepository;

    public ValidationUpdateStatusTopic(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override //Impede alterações futuras e adição de novas respostas no tópico após status atualizado para SOLUCIONADO
    public void validate(Long topicId){

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        if(topic.isClosed()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The topic is already marked as resolved.");
        }
    }

    @Override
    public List<TopicOperationType> getOperationType() {
        return List.of(TopicOperationType.REPLY, TopicOperationType.UPDATE, TopicOperationType.UPDATE_STATUS);
    }
}
