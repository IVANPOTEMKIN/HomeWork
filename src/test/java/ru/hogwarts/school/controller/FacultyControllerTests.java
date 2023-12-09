package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import ru.hogwarts.school.model.Faculty;

import static ru.hogwarts.school.utils.Examples.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTests {

    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate template;

    @Test
    void contextLoads() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    void addGriffindorInDB() {
        GRIFFINDOR.setId(GRIFFINDOR_ID);
        facultyController.add(GRIFFINDOR_NAME, GRIFFINDOR_COLOR);
    }

    @Test
    void testAdd_success() {
        GRIFFINDOR.setId(GRIFFINDOR_ID);

        Assertions
                .assertThat(this.template.postForObject("http://localhost:" + port
                                + "/faculty?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR,
                        GRIFFINDOR,
                        Faculty.class))
                .isNotNull()
                .isEqualTo(GRIFFINDOR);
    }

    @Test
    void testAdd_FacultyAlreadyAddedException() {
        addGriffindorInDB();

        String expected = "Code: 400 BAD_REQUEST. Error: ФАКУЛЬТЕТ УЖЕ ДОБАВЛЕН!";

        Assertions
                .assertThat(this.template.postForObject("http://localhost:" + port
                                + "/faculty?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR,
                        GRIFFINDOR,
                        String.class))
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void testAdd_InvalideInputException() {
        Assertions
                .assertThat(this.template.postForObject("http://localhost:" + port
                                + "/faculty?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY,
                        GRIFFINDOR,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGet_success() {
        addGriffindorInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        Faculty.class))
                .isNotNull()
                .isEqualTo(GRIFFINDOR);
    }

    @Test
    void testGet_FacultyNotFoundException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_FACULTY_NOT_FOUND);
    }

    @Test
    void testGet_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + INVALIDE_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetAll_success() {
        addGriffindorInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/all",
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testGetByNameOrColor_WithOnlyName_success() {
        addGriffindorInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?name=" + GRIFFINDOR_NAME,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testGetByNameOrColor_WithOnlyName_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?name=" + INVALIDE_NAME_FACULTY,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetByNameOrColor_WithOnlyColor_success() {
        addGriffindorInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?color=" + GRIFFINDOR_COLOR,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testGetByNameOrColor_WithOnlyColor_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?color=" + INVALIDE_COLOR_FACULTY,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetByNameOrColor_WithAllParameters_success() {
        addGriffindorInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testGetByNameOrColor_WithAllParameters_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetByNameOrColor_WithoutParameters_success() {
        addGriffindorInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty",
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void getStudents_success() {
        addGriffindorInDB();

        HARRY.setId(HARRY_ID);
        HARRY.setFaculty(GRIFFINDOR);
        studentController.add(HARRY_NAME, HARRY_AGE, GRIFFINDOR_ID);

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?id=" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void getStudents_FacultyNotFoundException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?id=" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_FACULTY_NOT_FOUND);
    }

    @Test
    void getStudents_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty?id=" + INVALIDE_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testEdit_WithOnlyName_success() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID
                                + "?name=" + SLYTHERIN_NAME,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        SLYTHERIN_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(GRIFFINDOR_NAME);
    }

    @Test
    void testEdit_WithOnlyName_InvalideInputException() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID
                                + "?name=" + INVALIDE_NAME_FACULTY,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_NAME_FACULTY);
    }

    @Test
    void testEdit_WithOnlyColor_success() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID
                                + "?color=" + SLYTHERIN_COLOR,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        SLYTHERIN_COLOR)
                .doesNotContain(GRIFFINDOR_COLOR);
    }

    @Test
    void testEdit_WithOnlyColor_InvalideInputException() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID
                                + "?color=" + INVALIDE_COLOR_FACULTY,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_COLOR_FACULTY);
    }

    @Test
    void testEdit_WithoutParameters_success() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        Faculty.class))
                .isNotNull()
                .isEqualTo(GRIFFINDOR);
    }

    @Test
    void testEdit_WithAllParameters_success() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID
                                + "?name=" + SLYTHERIN_NAME
                                + "&color=" + SLYTHERIN_COLOR,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR)
                .doesNotContain(GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testEdit_WithAllParameters_InvalideInputException() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID
                                + "?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_NAME_FACULTY,
                        INVALIDE_COLOR_FACULTY);
    }

    @Test
    void testRemove_success() {
        addGriffindorInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/faculty/" + GRIFFINDOR_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_FACULTY_NOT_FOUND);
    }
}