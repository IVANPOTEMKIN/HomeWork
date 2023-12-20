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
import ru.hogwarts.school.model.Student;

import java.text.DecimalFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.utils.Examples.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private StudentController studentController;
    @Autowired
    private FacultyController facultyController;

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

    private void addDraco() {
        Long id = studentController.add(DRACO_NAME, DRACO_AGE, SLYTHERIN.getId()).getId();
        DRACO.setId(id);
        DRACO.setFaculty(SLYTHERIN);
    }

    private void deleteFaculty(Long id) {
        facultyController.remove(id);
    }

    private void deleteStudent(Long id) {
        studentController.remove(id);
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    void add_success() {
        addGriffindor();

        HARRY.setFaculty(GRIFFINDOR);

        Student actual = this.template.postForObject("http://localhost:" + port
                        + "/student"
                        + "?name=" + HARRY_NAME
                        + "&age=" + HARRY_AGE
                        + "&facultyId=" + GRIFFINDOR.getId(),
                HARRY,
                Student.class);

        HARRY.setId(actual.getId());

        assertNotNull(actual);
        assertEquals(HARRY, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void add_InvalideInputException() {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/student"
                        + "?name=" + INVALIDE_NAME_STUDENT
                        + "&age=" + INVALIDE_AGE_STUDENT
                        + "&facultyId=" + INVALIDE_ID,
                HARRY,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void add_StudentAlreadyAddedException() {
        addGriffindor();
        addHarry();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/student"
                        + "?name=" + HARRY_NAME
                        + "&age=" + HARRY_AGE
                        + "&facultyId=" + GRIFFINDOR.getId(),
                HARRY,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_ALREADY_ADDED, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void get_success() {
        addGriffindor();
        addHarry();

        Student actual = this.template.getForObject("http://localhost:" + port
                        + "/student/" + HARRY.getId(),
                Student.class);

        assertNotNull(actual);
        assertEquals(HARRY, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void get_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student/" + INVALIDE_ID,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void get_StudentNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student/" + HARRY_ID,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_NOT_FOUND, actual);
    }

    @Test
    void getAll_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student/all",
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(HARRY.getId().toString()));
        assertTrue(actual.contains(HARRY.getName()));
        assertTrue(actual.contains(HARRY.getAge().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        assertTrue(actual.contains(DRACO.getId().toString()));
        assertTrue(actual.contains(DRACO.getName()));
        assertTrue(actual.contains(DRACO.getAge().toString()));
        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN.getName()));
        assertTrue(actual.contains(SLYTHERIN.getColor()));

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getLastFiveStudents_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student/last-five-students",
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(HARRY.getId().toString()));
        assertTrue(actual.contains(HARRY.getName()));
        assertTrue(actual.contains(HARRY.getAge().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        assertTrue(actual.contains(DRACO.getId().toString()));
        assertTrue(actual.contains(DRACO.getName()));
        assertTrue(actual.contains(DRACO.getAge().toString()));
        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN.getName()));
        assertTrue(actual.contains(SLYTHERIN.getColor()));

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getAmountAllFaculties_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        Integer expected = List.of(HARRY, DRACO).size();

        Integer actual = this.template.getForObject("http://localhost:" + port
                        + "/student/amount-all-students",
                Integer.class);

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getByAge_WithOnlyFirstParameter_success() {
        addGriffindor();
        addHarry();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student"
                        + "?minAge=" + HARRY_AGE,
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
    void getByAge_WithOnlyFirstParameter_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student"
                        + "?minAge=" + INVALIDE_AGE_STUDENT,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getByAge_WithOnlySecondParameter_success() {
        addSlytherin();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student"
                        + "?maxAge=" + DRACO_AGE,
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(DRACO.getId().toString()));
        assertTrue(actual.contains(DRACO.getName()));
        assertTrue(actual.contains(DRACO.getAge().toString()));
        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN.getName()));
        assertTrue(actual.contains(SLYTHERIN.getColor()));

        deleteStudent(DRACO.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student"
                        + "?maxAge=" + INVALIDE_AGE_STUDENT,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getByAge_WithAllParameters_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student"
                        + "?minAge=" + HARRY_AGE
                        + "&maxAge=" + DRACO_AGE,
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(HARRY.getId().toString()));
        assertTrue(actual.contains(HARRY.getName()));
        assertTrue(actual.contains(HARRY.getAge().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        assertTrue(actual.contains(DRACO.getId().toString()));
        assertTrue(actual.contains(DRACO.getName()));
        assertTrue(actual.contains(DRACO.getAge().toString()));
        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN.getName()));
        assertTrue(actual.contains(SLYTHERIN.getColor()));

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getByAge_WithAllParameters_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student"
                        + "?minAge=" + INVALIDE_AGE_STUDENT
                        + "&maxAge=" + INVALIDE_AGE_STUDENT,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getByAge_WithoutParameters_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student",
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(HARRY.getId().toString()));
        assertTrue(actual.contains(HARRY.getName()));
        assertTrue(actual.contains(HARRY.getAge().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        assertTrue(actual.contains(DRACO.getId().toString()));
        assertTrue(actual.contains(DRACO.getName()));
        assertTrue(actual.contains(DRACO.getAge().toString()));
        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN.getName()));
        assertTrue(actual.contains(SLYTHERIN.getColor()));

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getByName_success() {
        addGriffindor();
        addHarry();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student?name=" + HARRY_NAME,
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
    void getByName_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student?name=" + INVALIDE_NAME_STUDENT,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getByName_WithoutParameters_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student",
                String.class);

        assertNotNull(actual);

        assertTrue(actual.contains(HARRY.getId().toString()));
        assertTrue(actual.contains(HARRY.getName()));
        assertTrue(actual.contains(HARRY.getAge().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getId().toString()));
        assertTrue(actual.contains(GRIFFINDOR.getName()));
        assertTrue(actual.contains(GRIFFINDOR.getColor()));

        assertTrue(actual.contains(DRACO.getId().toString()));
        assertTrue(actual.contains(DRACO.getName()));
        assertTrue(actual.contains(DRACO.getAge().toString()));
        assertTrue(actual.contains(SLYTHERIN.getId().toString()));
        assertTrue(actual.contains(SLYTHERIN.getName()));
        assertTrue(actual.contains(SLYTHERIN.getColor()));

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getAvgAgeStudents_success() {
        DecimalFormat numberFormat = new DecimalFormat("#.#");

        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        Double expected = Double.valueOf(numberFormat.format((double) ((HARRY_AGE + DRACO_AGE) / List.of(HARRY, DRACO).size())));

        Double actual = this.template.getForObject("http://localhost:" + port
                        + "/student/average-age-students",
                Double.class);

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getAvgAgeAllStudents_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        Double expected = (double) (HARRY_AGE + DRACO_AGE) / List.of(HARRY, DRACO).size();

        Double actual = this.template.getForObject("http://localhost:" + port
                        + "/student/average-age-all-students",
                Double.class);

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getBySortedName_WithPrefix_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student/sorted-by-name?prefix=" + PREFIX,
                String.class);

        assertNotNull(actual);
        assertTrue(actual.contains(HARRY.getName().toUpperCase()));

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getBySortedName_WithPrefix_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student/sorted-by-name?prefix=" + INVALIDE_PREFIX,
                String.class);

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);
    }

    @Test
    void getBySortedName_WithoutPrefix_success() {
        addGriffindor();
        addSlytherin();
        addHarry();
        addDraco();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/student/sorted-by-name",
                String.class);

        assertNotNull(actual);
        assertTrue(actual.contains(HARRY.getName().toUpperCase()));
        assertTrue(actual.contains(DRACO.getName().toUpperCase()));

        deleteStudent(HARRY.getId());
        deleteStudent(DRACO.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void getFaculty_success() {
        addGriffindor();
        addHarry();

        Faculty actual = this.template.getForObject("http://localhost:" + port
                        + "/student?id=" + HARRY.getId(),
                Faculty.class);

        assertNotNull(actual);
        assertEquals(GRIFFINDOR, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyName_success() {
        addGriffindor();
        addHarry();

        Student expected = new Student(DRACO_NAME, HARRY_AGE, GRIFFINDOR);
        expected.setId(HARRY.getId());

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + DRACO_NAME,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + INVALIDE_NAME_STUDENT,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyName_StudentAlreadyAddedException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + HARRY_NAME,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_ALREADY_ADDED, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyNameAndAge_success() {
        addGriffindor();
        addHarry();

        Student expected = new Student(DRACO_NAME, DRACO_AGE, GRIFFINDOR);
        expected.setId(HARRY.getId());

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + DRACO_NAME
                        + "&age=" + DRACO_AGE,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyNameAndAge_InvalideInputException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + INVALIDE_NAME_STUDENT
                        + "&age=" + INVALIDE_AGE_STUDENT,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyNameAndAge_StudentAlreadyAddedException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + HARRY_NAME
                        + "&age=" + HARRY_AGE,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_ALREADY_ADDED, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_success() {
        addGriffindor();
        addSlytherin();
        addHarry();

        Student expected = new Student(DRACO_NAME, HARRY_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + DRACO_NAME
                        + "&facultyId=" + SLYTHERIN.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_InvalideInputException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + INVALIDE_NAME_STUDENT
                        + "&facultyId=" + INVALIDE_ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_StudentAlreadyAddedException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + HARRY_NAME
                        + "&facultyId=" + GRIFFINDOR.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_ALREADY_ADDED, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyAge_success() {
        addGriffindor();
        addHarry();

        Student expected = new Student(HARRY_NAME, DRACO_AGE, GRIFFINDOR);
        expected.setId(HARRY.getId());

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?age=" + DRACO_AGE,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyAge_InvalideInputException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?age=" + INVALIDE_AGE_STUDENT,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyAge_StudentAlreadyAddedException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?age=" + HARRY_AGE,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_ALREADY_ADDED, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_success() {
        addGriffindor();
        addSlytherin();
        addHarry();

        Student expected = new Student(HARRY_NAME, DRACO_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?age=" + DRACO_AGE
                        + "&facultyId=" + SLYTHERIN.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_InvalideInputException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?age=" + INVALIDE_AGE_STUDENT
                        + "&facultyId=" + INVALIDE_ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_StudentAlreadyAddedException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?age=" + HARRY_AGE
                        + "&facultyId=" + GRIFFINDOR.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_ALREADY_ADDED, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithOnlyFacultyId_success() {
        addGriffindor();
        addSlytherin();
        addHarry();

        Student expected = new Student(HARRY_NAME, HARRY_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?facultyId=" + SLYTHERIN.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void edit_WithAllParameters_success() {
        addGriffindor();
        addSlytherin();
        addHarry();

        Student expected = new Student(DRACO_NAME, DRACO_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + DRACO_NAME
                        + "&age=" + DRACO_AGE
                        + "&facultyId=" + SLYTHERIN.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(expected, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
        deleteFaculty(SLYTHERIN.getId());
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + INVALIDE_NAME_STUDENT
                        + "&age=" + INVALIDE_AGE_STUDENT
                        + "&facultyId=" + INVALIDE_ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_INVALIDE_DATES, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithAllParameters_StudentAlreadyAddedException() {
        addGriffindor();
        addHarry();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId()
                        + "?name=" + HARRY_NAME
                        + "&age=" + HARRY_AGE
                        + "&facultyId=" + GRIFFINDOR.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(MESSAGE_STUDENT_ALREADY_ADDED, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void edit_WithoutParameters_success() {
        addGriffindor();
        addHarry();

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(HARRY, actual);

        deleteStudent(HARRY.getId());
        deleteFaculty(GRIFFINDOR.getId());
    }

    @Test
    void remove_success() {
        addGriffindor();
        addHarry();

        Student actual = this.template.exchange("http://localhost:" + port
                        + "/student/" + HARRY.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Student.class).getBody();

        assertNotNull(actual);
        assertEquals(HARRY, actual);

        deleteFaculty(GRIFFINDOR.getId());
    }
}