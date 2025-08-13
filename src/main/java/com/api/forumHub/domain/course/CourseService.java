package com.api.forumHub.domain.course;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseDTO saveCourse(CourseRequest request) {

        Course course = new Course();
        course.setName(request.name());

        courseRepository.save(course);

        return CourseMapper.toDto(course);
    }

    public List<CourseDTO> getCourseName(String name) {
        List<Course> courses = courseRepository.findByNameContainingIgnoreCase(name);

        if (courses.isEmpty()) {
            throw new EntityNotFoundException("No courses found with the name: " + name);
        }

        return courses.stream().map(CourseMapper::toDto)
                .toList();
    }

    public List<CourseDTO> getCourses() {
        return courseRepository.findAll().stream().map(CourseMapper::toDto).toList();
    }

    public void deleteCourse(Long id) {
        courseRepository.findById(id).ifPresentOrElse(
                courseRepository::delete,
                () -> {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found by id: " + id); }
        );
    }
}
