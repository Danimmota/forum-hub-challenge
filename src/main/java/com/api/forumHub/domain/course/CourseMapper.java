package com.api.forumHub.domain.course;

import com.api.forumHub.domain.answer.Answer;
import com.api.forumHub.domain.answer.AnswerDTO;
import com.api.forumHub.domain.answer.AnswerMapper;
import com.api.forumHub.domain.topic.Topic;
import com.api.forumHub.domain.topic.TopicResponseDTO;
import com.api.forumHub.domain.user.UserMapper;

import java.util.List;

public class CourseMapper {

    public static Course toEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.id());
        course.setName(dto.name());

        List<Topic> topics = (dto.topics() == null ? List.<TopicResponseDTO>of() : dto.topics())
                .stream().map(t -> {

            Topic topic = new Topic();
            topic.setId(t.id());
            topic.setTitle(t.title());
            topic.setMessage(t.message());
            topic.setCreationDate(t.creationDate());
            topic.setStatus(t.status());
            topic.setCourse(course);
            topic.setAuthor(UserMapper.toUserDtoEntity(t.author()));

            List<Answer> answers = (t.answers() == null ? List.<AnswerDTO>of() : t.answers())
                    .stream().map(
                    answerDTO -> AnswerMapper.toEntity(answerDTO)
            ).toList();

            topic.setAnswers(answers);

            return topic;
        }).toList();

        course.setTopics(topics);

        return course;
    }

    public static CourseDTO toDto(Course course) {
        List<TopicResponseDTO> topicDTOs = course.getTopics().stream().map(t -> {
            List<AnswerDTO> answerDTOs = t.getAnswers().stream()
                    .map(AnswerMapper::toDto)
                    .toList();
            return new TopicResponseDTO(
                    t.getId(),
                    t.getTitle(),
                    t.getMessage(),
                    t.getCreationDate(),
                    t.getStatus(),
                    CourseMapper.toDto(t.getCourse()),
                    UserMapper.toDto(t.getAuthor()),
                    answerDTOs
            );
        }).toList();

        return new CourseDTO(
                course.getId(),
                course.getName(),
                topicDTOs
        );
    }
}
