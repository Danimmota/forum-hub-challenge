package com.api.forumHub.controller;

import com.api.forumHub.domain.answer.AnswerRequest;
import com.api.forumHub.domain.answer.AnswerResponseDTO;
import com.api.forumHub.domain.answer.AnswerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/answers")
@SecurityRequirement(name = "bearer-key")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

//    @PostMapping
//    public ResponseEntity<AnswerResponseDTO> createAnswer(
//            @RequestBody @Valid AnswerRequest request,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        AnswerResponseDTO responseAnswer = answerService.createAnswer(request, userDetails.getUsername());
//
//        URI uri = URI.create("/answers/" + responseAnswer.id());
//
//        return ResponseEntity.created(uri).body(responseAnswer);
//    }

    @GetMapping("/{topicId}")
    public ResponseEntity<List<AnswerResponseDTO>> listAnswersByTopicOrderByDate(@PathVariable Long topicId) {
        List<AnswerResponseDTO> answers = answerService.listAnswersByTopicOrderByDate(topicId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponseDTO>> listAnswers(){
        return ResponseEntity.ok(answerService.listAnswers());
    }

    @GetMapping("/answers/{authorId}")
    public ResponseEntity<List<AnswerResponseDTO>> listAnswerByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(answerService.listAnswersByAuthor(authorId));
    }

    @DeleteMapping("{answerId}")
    public ResponseEntity<Void> deleteAnswer (@PathVariable Long answerId, @AuthenticationPrincipal UserDetails userDetails) {
        answerService.deleteAnswer(answerId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
