package com.api.forumHub.domain.topic.validation;

import java.util.List;

public interface TopicValidator {

    void validate(Long topicId);

    List<TopicOperationType> getOperationType();
}
