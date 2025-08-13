package com.api.forumHub.controller;

import com.api.forumHub.domain.answer.AnswerRequest;
import com.api.forumHub.domain.answer.AnswerResponseDTO;
import com.api.forumHub.domain.topic.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/topics")
@SecurityRequirement(name = "bearer-key")
public class TopicsController {

    private final TopicService topicService;

    public TopicsController(TopicService topicService) {

        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<TopicResponseDTO> createTopic(
            @RequestBody @Valid TopicRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        TopicResponseDTO responseTopic = topicService.createTopic(request, userDetails.getUsername());

        URI uri = URI.create("/topics/" + responseTopic.id());

        return ResponseEntity.created(uri).body(responseTopic);
    }

    @PostMapping("/{topicId}/answers")
    public ResponseEntity<AnswerResponseDTO> replyTopic(
            @PathVariable Long topicId,
            @RequestBody @Valid AnswerRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        AnswerResponseDTO responseAnswer = topicService.replyTopic(topicId, request, userDetails.getUsername());

        URI uri = URI.create("/topics/" + topicId +"/answers/" + responseAnswer.id());

        return ResponseEntity.created(uri).body(responseAnswer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicResponseDTO> updateTopic(
            @PathVariable Long id,
            @RequestBody @Valid TopicUpdateRequest request) {

        return ResponseEntity.ok(topicService.updateTopic(id, request));
    }

    @PutMapping("/topic/{topicId}")
    public ResponseEntity<TopicResponseDTO> updateStatusTopic(
            @PathVariable Long topicId,
            @RequestBody @Valid TopicUpdateStatusRequest request) {

        return ResponseEntity.ok(topicService.updateStatusTopic(topicId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicResponseDTO> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopicResponse(id));
    }

    @GetMapping
    public ResponseEntity<Page<TopicResponseDTO>> getListTopics(
            @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pagination) {
        return ResponseEntity.ok(topicService.getListTopics(pagination));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TopicResponseDTO>> getTopicByCourseName(
            @RequestParam String courseName,
            @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pagination) {

        return ResponseEntity.ok(topicService.getTopicsByCourseName(courseName, pagination));
    }

    @GetMapping("/topics/{authorId}")
    public ResponseEntity<Page<TopicResponseDTO>> getTopicByAuthor(
            @RequestParam Long authorId,
            @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pagination) {

        return ResponseEntity.ok(topicService.getTopicsByAuthor(authorId, pagination));
    }

    @GetMapping("/term") //GET /topics/search?term=java
    public ResponseEntity<List<TopicResponseDTO>> getTopicsByTermInTitle(@RequestParam String term) {

        return ResponseEntity.ok(topicService.getTopicsByTermTitle(term));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id){

        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
}
