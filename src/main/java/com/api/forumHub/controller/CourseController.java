package com.api.forumHub.controller;

import com.api.forumHub.domain.course.CourseDTO;
import com.api.forumHub.domain.course.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courses")
@SecurityRequirement(name = "bearer-key")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CourseDTO> created(@RequestBody @Valid CourseDTO courseDTO) {
        CourseDTO newCourse = courseService.saveCourse(courseDTO);

        URI uri = URI.create("/courses/" + newCourse.id());

        return ResponseEntity.created(uri).body(newCourse);
    }

    @GetMapping("/search")
    public ResponseEntity<CourseDTO> getCourseName(@RequestParam String name) {
        CourseDTO course = courseService.getCourseName(name);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getCourses() {
        List<CourseDTO> courseDTOS = courseService.getCourses();
        return ResponseEntity.ok(courseDTOS);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
