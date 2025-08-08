package com.api.forumHub.domain.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> existsByCourseName(String courseName, Pageable pagination);

    Page<Topic> existsByAuthorId(Long authorId, Pageable pagination);

    @Query("SELECT t FROM Topic t WHERE LOWER(t.title) LIKE LOWER(%:termInTheTitle%)")
    List<Topic> topicByTermInTheTitle(String termInTheTitle);
}
