package ru.hogwarts.school.controller.TestRestTemplate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.utils.Examples.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentController studentController;

    private void addGriffindor() {
        Long id = facultyController.add(GRIFFINDOR_NAME, GRIFFINDOR_COLOR).getId();
        GRIFFINDOR.setId(id);
    }

    private void addSlytherin() {
        Long id = facultyController.add(SLYTHERIN_NAME, SLYTHERIN_COLOR).getId();
        SLYTHERIN.setId(id);
    }

    private void addHarry() {
        Long id = studentController.add(HARRY_NAME, HARRY_AGE, GRIFFINDOR.getId()).getId();
        HARRY.setId(id);
        HARRY.setFaculty(GRIFFINDOR);
    }

    private void deleteFaculty(Long id) {
        facultyController.remove(id);
    }

    private void deleteStudent(Long id) {
        studentController.remove(id);
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void add_success() {
        Faculty actual = this.template.postForObject("http://localhost:" + port
                        + "/faculty"
                        + "?name=" + GRIFFINDOR_NAME
                        + "&color=" + GRIFFINDOR_COLOR,
                GRIFFINDOR,
                Faculty.class);

        GRIFFINDOR.setId(actual.getId());

        assertNotNull(actual);
        assertEquals(GRIFFINDOR, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void add_InvalideInputException() {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/faculty"
                        + "?name=" + INVALIDE_NAME_FACULTY
                        + "&color=" + INVALIDE_COLOR_FACULTY,
                GRIFFINDOR,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void add_FacultyAlreadyAddedException() {
        addGriffindor();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/faculty"
                        + "?name=" + GRIFFINDOR_NAME
                        + "&color=" + GRIFFINDOR_COLOR,
                GRIFFINDOR,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_FACULTY_ALREADY_ADDED, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void get_success() {
        addGriffindor();

        Faculty actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId(),
                Faculty.class);

        assertNotNull(actual);
        assertEquals(GRIFFINDOR, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void get_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty/" + INVALIDE_ID,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void get_FacultyNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR_ID,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_FACULTY_NOT_FOUND, actual);
    }

    @Test
    void getAll_success() {
        addGriffindor();
        addSlytherin();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty/all",
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN.getName()));
        assertTrue(actual.contains(SLYTHERIN.getColor()));

        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getAmountAllFaculties_success() {
        addGriffindor();
        addSlytherin();

        Integer expected = List.of(GRIFFINDOR, SLYTHERIN).size();

        Integer actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty/amount-all-faculties",
                Integer.class);

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getByNameOrColor_WithOnlyName_success() {
        addGriffindor();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty"
                        + "?name=" + GRIFFINDOR_NAME,
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void getByNameOrColor_WithOnlyName_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty"
                        + "?name=" + INVALIDE_NAME_FACULTY,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getByNameOrColor_WithOnlyColor_success() {
        addGriffindor();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty"
                        + "?color=" + GRIFFINDOR_COLOR,
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void getByNameOrColor_WithOnlyColor_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty"
                        + "?color=" + INVALIDE_COLOR_FACULTY,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getByNameOrColor_WithAllParameters_success() {
        addGriffindor();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty"
                        + "?name=" + GRIFFINDOR_NAME
                        + "&color=" + GRIFFINDOR_COLOR,
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void getByNameOrColor_WithAllParameters_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty"
                        + "?name=" + INVALIDE_NAME_FACULTY
                        + "&color=" + INVALIDE_COLOR_FACULTY,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getByNameOrColor_WithoutParameters_success() {
        addGriffindor();
        addSlytherin();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty",
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR_NAME));
        assertTrue(actual.contains(GRIFFINDOR_COLOR));

        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN_NAME));
        assertTrue(actual.contains(SLYTHERIN_COLOR));

        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getStudents_success() {
        addGriffindor();
        addHarry();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/faculty?id=" + GRIFFINDOR.getId(),
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(HARRY.getId().toString()));
        assertTrue(actual.contains(HARRY.getName()));
        assertTrue(actual.contains(HARRY.getAge().toString()));

        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyName_success() {
        addGriffindor();

        Faculty expected = new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR);
        expected.setId(GRIFFINDOR.getId());

        Faculty actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?name=" + SLYTHERIN_NAME,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Faculty.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        addGriffindor();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?name=" + INVALIDE_NAME_FACULTY,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyName_FacultyAlreadyAddedException() {
        addGriffindor();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?name=" + GRIFFINDOR_NAME,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_FACULTY_ALREADY_ADDED, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyColor_success() {
        addGriffindor();

        Faculty expected = new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR);
        expected.setId(GRIFFINDOR.getId());

        Faculty actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?color=" + SLYTHERIN_COLOR,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Faculty.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyColor_InvalideInputException() {
        addGriffindor();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?color=" + INVALIDE_COLOR_FACULTY,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyColor_FacultyAlreadyAddedException() {
        addGriffindor();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?color=" + GRIFFINDOR_COLOR,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_FACULTY_ALREADY_ADDED, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithAllParameters_success() {
        addGriffindor();

        Faculty expected = new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR);
        expected.setId(GRIFFINDOR.getId());

        Faculty actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?name=" + SLYTHERIN_NAME
                        + "&color=" + SLYTHERIN_COLOR,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Faculty.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        addGriffindor();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?name=" + INVALIDE_NAME_FACULTY
                        + "&color=" + INVALIDE_COLOR_FACULTY,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithAllParameters_FacultyAlreadyAddedException() {
        addGriffindor();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId()
                        + "?name=" + GRIFFINDOR_NAME
                        + "&color=" + GRIFFINDOR_COLOR,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_FACULTY_ALREADY_ADDED, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithoutParameters_success() {
        addGriffindor();

        Faculty actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Faculty.class).getBody();

        assertNotNull(actual);
        assertEquals(GRIFFINDOR, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void remove_success() {
        addGriffindor();

        Faculty actual = this.template.exchange("http://localhost:" + port
                        + "/faculty/" + GRIFFINDOR.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Faculty.class).getBody();

        assertNotNull(actual);
        assertEquals(GRIFFINDOR, actual);
    }
}