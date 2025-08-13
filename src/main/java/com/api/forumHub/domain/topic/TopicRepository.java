package com.api.forumHub.domain.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("""
            SELECT t
            FROM Topic t
            JOIN t.course c
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :courseName, '%'))
            """)
    Page<Topic> findByCourseNameIgnoreCase(@Param("courseName") String courseName, Pageable pagination);

    Page<Topic> existsByAuthorId(Long authorId, Pageable pagination);

    @Query("SELECT t FROM Topic t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :termInTheTitle, '%'))")
    List<Topic> topicByTermInTheTitle(String termInTheTitle);
}
