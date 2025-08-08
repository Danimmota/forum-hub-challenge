package com.api.forumHub.controller;

import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.answer.AnswerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
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

    @PostMapping
    @Transactional
    public ResponseEntity<AnswerDTO> createAnswer(
            @RequestBody @Valid AnswerDTO answerDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AnswerDTO responseAnswer = answerService.createAnswer(answerDTO, userDetails.getUsername());

        URI uri = URI.create("/answers/" + responseAnswer.id());

        return ResponseEntity.created(uri).body(responseAnswer);
    }

    @GetMapping("/answers/{topicId}")
    public ResponseEntity<List<AnswerDTO>> listAnswersByTopicOrderByDate(@PathVariable Long topicId) {
        List<AnswerDTO> answers = answerService.listAnswersByTopicOrderByDate(topicId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping
    public ResponseEntity<List<AnswerDTO>> listAnswers(){
        return ResponseEntity.ok(answerService.listAnswers());
    }

    @GetMapping("/answers/{authorId}")
    public ResponseEntity<List<AnswerDTO>> listAnswerByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(answerService.listAnswersByAuthor(authorId));
    }
}
