package com.api.forumHub.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByTopicIdOrderByCreationDateDesc(Long topicId);

    @Query("SELECT a FROM Answer a WHERE a.author.id = :authorId ORDER BY a.creationDate DESC")
    List<Answer> findAnswerByAuthor(@Param("autorId") Long authorId);
}
