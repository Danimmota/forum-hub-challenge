package com.api.forumHub.controller;

import com.api.forumHub.domain.course.Course;
import com.api.forumHub.domain.course.CourseDTO;
import com.api.forumHub.domain.course.CourseRepository;
import com.api.forumHub.domain.topic.*;
import com.api.forumHub.domain.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<TopicRequest> topicRequestJson;

    @Autowired
    private JacksonTester<TopicResponseDTO> topicResponseDTOJson;

    @MockitoBean
    private TopicRepository topicRepository;

    @MockitoBean
    private CourseRepository courseRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @DisplayName("Deveria retornar codigo http 403 quando usuário não estiver autorizado a criar um tópico")
    void createTopicCenario01() throws Exception{

        MockHttpServletResponse response = mvc
                .perform(post("/topics"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("Deveria retornar codigo http 201 quando informacoes estão validas")
    @WithMockUser(username = "teste@email.com", roles = {"USER"})
    void createTopicCenario02() throws Exception{

        var topicRequest = new TopicRequest(
                "Programação Orientada a Objetos e Java",
                "Quais os princípios da programação orientada a objetos e seu uso em Java?",
                courseDTO().id());


        User mockUser = new User();
        mockUser.setId(userResponseDTO().id());
        mockUser.setName(userResponseDTO().name());
        mockUser.setEmail(userResponseDTO().email());
        mockUser.setRole(userResponseDTO().role());

        when(userRepository.findUserByEmail("teste@email.com")).thenReturn(Optional.of(mockUser));

        var mockCourse = new Course();
        mockCourse.setId(courseDTO().id());
        mockCourse.setName(courseDTO().name());

        when(courseRepository.findById(courseDTO().id())).thenReturn(Optional.of(mockCourse));

        var savedTopic = new Topic();
        savedTopic.setId(10L);
        savedTopic.setTitle(topicRequest.title());
        savedTopic.setMessage(topicRequest.message());
        savedTopic.setCreationDate(LocalDateTime.now());
        savedTopic.setStatus(TopicStatus.PENDENTE);
        savedTopic.setCourse(mockCourse);
        savedTopic.setAuthor(mockUser);

        when(topicRepository.save(any())).thenReturn(savedTopic);

        var expectedResponse = TopicMapper.toResponseDto(savedTopic);

        MockHttpServletResponse response = mvc
                .perform(post("/topics")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(topicRequestJson.write(topicRequest).getJson()))
                .andReturn().getResponse();

        String jsonEsperado = topicResponseDTOJson.write(expectedResponse).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Deveria retornar codigo http 400 quando o topico estiver com Status SOLUCIONADO")
    @WithMockUser
    void replyTopic() throws Exception{
        var topic = new Topic();
        topic.setId(3L);
        topic.setStatus(TopicStatus.SOLUCIONADO);

        when(topicRepository.findById(3L)).thenReturn(Optional.of(topic));

        MockHttpServletResponse response = mvc
                .perform(post("/topics/3/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"Minha resposta\"}"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private CourseDTO courseDTO() {
        return new CourseDTO(
                6L,
                "POO - Programação Orientada a Objetos",
                null
        );
    }

    private UserResponseDTO userResponseDTO() {
        return new UserResponseDTO(
                1L,
                "Teste",
                "teste@email.com",
                Role.ROLE_USER
        );
    }
}