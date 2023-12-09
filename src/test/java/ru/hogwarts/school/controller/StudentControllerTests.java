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
class StudentControllerTests {

    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate template;

    void addHarryInDB() {
        HARRY.setId(HARRY_ID);
        studentController.add(HARRY_NAME, HARRY_AGE, GRIFFINDOR_ID);
    }

    void addGriffindorInDB() {
        GRIFFINDOR.setId(GRIFFINDOR_ID);
        facultyController.add(GRIFFINDOR_NAME, GRIFFINDOR_COLOR);
        HARRY.getFaculty().setId(GRIFFINDOR_ID);
    }

    void addSlytherinInDB() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        facultyController.add(SLYTHERIN.getName(), SLYTHERIN.getColor());
        DRACO.getFaculty().setId(SLYTHERIN_ID);
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void testAdd_success() {
        addGriffindorInDB();

        HARRY.setId(HARRY_ID);
        HARRY.getFaculty().setId(GRIFFINDOR_ID);

        Assertions
                .assertThat(this.template.postForObject("http://localhost:" + port
                                + "/student?name=" + HARRY_NAME
                                + "&age=" + HARRY_AGE
                                + "&facultyId=" + GRIFFINDOR_ID,
                        HARRY,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString());
    }

    @Test
    void testAdd_StudentAlreadyAddedException() {
        addGriffindorInDB();
        addHarryInDB();

        String expected = "Code: 400 BAD_REQUEST. Error: СТУДЕНТ УЖЕ ДОБАВЛЕН!";

        Assertions
                .assertThat(this.template.postForObject("http://localhost:" + port
                                + "/student?name=" + HARRY_NAME
                                + "&age=" + HARRY_AGE
                                + "&facultyId=" + GRIFFINDOR_ID,
                        HARRY,
                        String.class))
                .isNotNull()
                .isEqualTo(expected);
    }

    @Test
    void testAdd_InvalideInputException() {
        Assertions
                .assertThat(this.template.postForObject("http://localhost:" + port
                                + "/student?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID,
                        HARRY,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGet_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
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
    void testGet_StudentNotFoundException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_STUDENT_NOT_FOUND);
    }

    @Test
    void testGet_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + INVALIDE_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetAll_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/all",
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
    void testGetByAge_WithOnlyFirstParameter_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student"
                                + "?minAge=" + HARRY_AGE,
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
    void testGetByAge_WithOnlyFirstParameter_InvalideInputException() {
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student"
                                + "?minAge=" + INVALIDE_AGE_STUDENT,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetByAge_WithOnlySecondParameter_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student"
                                + "?maxAge=" + HARRY_AGE,
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
    void testGetByAge_WithOnlySecondParameter_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student"
                                + "?maxAge=" + INVALIDE_AGE_STUDENT,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetByAge_WithAllParameters_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student"
                                + "?minAge=" + HARRY_AGE
                                + "&maxAge=" + HARRY_AGE,
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
    void testGetByAge_WithAllParameters_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student"
                                + "?minAge=" + INVALIDE_AGE_STUDENT
                                + "&maxAge=" + INVALIDE_AGE_STUDENT,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testGetByAge_WithoutParameters_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student",
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
    void testGetFacultyById_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/?id=" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testGetFacultyById_StudentNotFoundException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/?id=" + HARRY_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_STUDENT_NOT_FOUND);
    }

    @Test
    void testGetFacultyById_InvalideInputException() {
        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/?id=" + INVALIDE_ID,
                        String.class))
                .isNotNull()
                .isEqualTo(MESSAGE_INVALIDE_DATES);
    }

    @Test
    void testEdit_WithOnlyName_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        DRACO_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(HARRY_NAME);
    }

    @Test
    void testEdit_WithOnlyName_InvalideInputException() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_NAME_STUDENT);
    }

    @Test
    void testEdit_WithOnlyNameAndAge_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME
                                + "&age=" + DRACO_AGE,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        DRACO_NAME,
                        DRACO_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(HARRY_NAME,
                        HARRY_AGE.toString());
    }

    @Test
    void testEdit_WithOnlyNameAndAge_InvalideInputException() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT.toString());
    }

    @Test
    void testEdit_WithOnlyNameAndFacultyId_success() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME
                                + "&facultyId=" + SLYTHERIN_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        DRACO_NAME,
                        HARRY_AGE.toString(),
                        SLYTHERIN_ID.toString(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR)
                .doesNotContain(HARRY_NAME,
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testEdit_WithOnlyNameAndFacultyId_InvalideInputException() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&facultyId=" + INVALIDE_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_NAME_STUDENT,
                        INVALIDE_ID.toString());
    }

    @Test
    void testEdit_WithOnlyAge_success() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?age=" + DRACO_AGE,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        DRACO_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(HARRY_AGE.toString());
    }

    @Test
    void testEdit_WithOnlyAge_InvalideInputException() {
        addGriffindorInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?age=" + INVALIDE_AGE_STUDENT,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        DRACO_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_AGE_STUDENT.toString());
    }

    @Test
    void testEdit_WithOnlyAgeAndFacultyId_success() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?age=" + DRACO_AGE
                                + "&facultyId=" + SLYTHERIN_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        DRACO_AGE.toString(),
                        SLYTHERIN_ID.toString(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR)
                .doesNotContain(HARRY_AGE.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testEdit_WithOnlyAgeAndFacultyId_InvalideInputException() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_AGE_STUDENT.toString(),
                        INVALIDE_ID.toString());
    }

    @Test
    void testEdit_WithOnlyFacultyId_success() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?facultyId=" + SLYTHERIN_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        SLYTHERIN_ID.toString(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR)
                .doesNotContain(GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testEdit_WithOnlyFacultyId_InvalideInputException() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?facultyId=" + INVALIDE_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_ID.toString());
    }

    @Test
    void testEdit_WithAllParameters_success() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME
                                + "&age=" + DRACO_AGE
                                + "&facultyId=" + SLYTHERIN_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        DRACO_NAME,
                        DRACO_AGE.toString(),
                        SLYTHERIN_ID.toString(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR)
                .doesNotContain(HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR);
    }

    @Test
    void testEdit_WithAllParameters_InvalideInputException() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        String.class))
                .isNotNull()
                .contains(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString(),
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR)
                .doesNotContain(INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT.toString(),
                        INVALIDE_ID.toString());
    }

    @Test
    void testEdit_WithoutParameters_success() {
        addGriffindorInDB();
        addSlytherinInDB();
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        HttpMethod.PUT,
                        HttpEntity.EMPTY,
                        String.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/" + HARRY_ID,
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
    void testRemove_success() {
        addHarryInDB();

        Assertions.
                assertThat(this.template.exchange("http://localhost:" + port
                                + "/student/" + HARRY_ID,
                        HttpMethod.DELETE,
                        HttpEntity.EMPTY,
                        Faculty.class)).
                isNotNull();

        Assertions
                .assertThat(this.template.getForObject("http://localhost:" + port
                                + "/student/all",
                        String.class))
                .isNotNull()
                .doesNotContain(HARRY_ID.toString(),
                        HARRY_NAME,
                        HARRY_AGE.toString(),
                        GRIFFINDOR_ID.toString());
    }
}