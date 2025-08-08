package com.api.forumHub.domain.course;

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

    public CourseDTO saveCourse(CourseDTO courseDTO) {
        Course course = courseRepository.save(CourseMapper.toEntity(courseDTO));
        return CourseMapper.toDto(course);
    }

    public CourseDTO getCourseName(String name) {
        Course course = courseRepository.findByName(name);

        return course != null ? CourseMapper.toDto(course) : null;
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
