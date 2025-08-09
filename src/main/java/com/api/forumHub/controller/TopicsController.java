package com.api.forumHub.controller;

import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.topic.TopicRequest;
import com.api.forumHub.domain.topic.TopicResponseDTO;
import com.api.forumHub.domain.topic.TopicService;
import com.api.forumHub.domain.topic.TopicUpdateRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    @Transactional
    public ResponseEntity<TopicResponseDTO> createTopic(
            @RequestBody @Valid TopicRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        TopicResponseDTO responseTopic = topicService.createTopic(request, userDetails.getUsername());

        URI uri = URI.create("/topics/" + responseTopic.id());

        return ResponseEntity.created(uri).body(responseTopic);
    }

    @PostMapping("/{topicId}/answers")
    @Transactional
    public ResponseEntity<AnswerDTO> replyTopic(
            @PathVariable Long topicId,
            @RequestBody @Valid AnswerDTO answer,
            @AuthenticationPrincipal UserDetails userDetails) {

        AnswerDTO responseAnswer = topicService.replyTopic(topicId, answer, userDetails.getUsername());

        URI uri = URI.create("/topics/" + topicId +"/answers/" + responseAnswer.id());

        return ResponseEntity.created(uri).body(responseAnswer);
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<TopicResponseDTO> updateTopic(
            @PathVariable Long id,
            @RequestBody @Valid TopicUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        TopicResponseDTO responseDTO = topicService.updateTopic(id, request, userDetails.getUsername());
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/topics/{topicId}")
    public ResponseEntity<TopicResponseDTO> updateStatusTopic(
            @PathVariable Long topicId,
            @AuthenticationPrincipal UserDetails userDetails) {

        TopicResponseDTO responseDTO = topicService.updateStatusTopic(topicId, userDetails.getUsername());
        return ResponseEntity.ok(responseDTO);
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

    @GetMapping("/{authorId}")
    public ResponseEntity<Page<TopicResponseDTO>> getTopicByAuthor(
            @RequestParam Long authorId,
            @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pagination) {

        return ResponseEntity.ok(topicService.getTopicsByAuthor(authorId, pagination));
    }

    @GetMapping("/term") //GET /topics/search?term=java
    public ResponseEntity<List<TopicResponseDTO>> getTopicsByTermInTitle(@RequestParam String term) {
        List<TopicResponseDTO> topics = topicService.getTopicsByTermTitle(term);

        return ResponseEntity.ok(topics);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id, Authentication authentication){

        String authenticatedUserEmail = authentication.getName();
        topicService.deleteTopic(id, authenticatedUserEmail);
        return ResponseEntity.noContent().build();
    }
}
