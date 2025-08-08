package com.api.forumHub.domain.topic.validation;

import com.api.forumHub.domain.topic.TopicRepository;

public class ValidationRegisteredUser {

    private final TopicRepository topicRepository;

    public ValidationRegisteredUser(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    //Apenas usuarios cadastrados podem criar novos topicos
    public void validar(){

    }
}
