package ru.hogwarts.school.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.AvatarServiceImpl;
import ru.hogwarts.school.service.impl.CheckServiceImpl;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.utils.Examples.*;

@WebMvcTest
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private AvatarRepository avatarRepository;
    @SpyBean
    private FacultyServiceImpl facultyService;
    @SpyBean
    private StudentServiceImpl studentService;
    @SpyBean
    private AvatarServiceImpl avatarService;
    @SpyBean
    private CheckServiceImpl checkService;
    @InjectMocks
    private FacultyController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private void getGriffindor() {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        GRIFFINDOR.setId(GRIFFINDOR_ID);
    }

    private void getSlytherin() {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(SLYTHERIN));
        SLYTHERIN.setId(SLYTHERIN_ID);
    }

    private void addHarry() {
        when(studentService.add(HARRY_NAME, HARRY_AGE, GRIFFINDOR.getId()))
                .thenReturn(HARRY);
        HARRY.setId(HARRY_ID);
        HARRY.setFaculty(GRIFFINDOR);
    }

    private void getHarry() {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        HARRY.setId(HARRY_ID);
        HARRY.setFaculty(GRIFFINDOR);
    }

    @Test
    void add_success() throws Exception {
        getGriffindor();
        addHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student"
                                + "?name=" + HARRY_NAME
                                + "&age=" + HARRY_AGE
                                + "&facultyId=" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(HARRY.getId()))
                .andExpect(jsonPath("$.name").value(HARRY.getName()))
                .andExpect(jsonPath("$.age").value(HARRY.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.faculty.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.faculty.color").value(GRIFFINDOR.getColor()));
    }

    @Test
    void add_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student"
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID
                        ))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void add_StudentAlreadyAddedException() throws Exception {
        getGriffindor();
        addHarry();

        when(studentService.getAll())
                .thenReturn(getStudents());

        String expected = "Code: 400 BAD_REQUEST. Error: СТУДЕНТ УЖЕ ДОБАВЛЕН!";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student"
                                + "?name=" + HARRY.getName()
                                + "&age=" + HARRY.getAge()
                                + "&facultyId=" + GRIFFINDOR.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void get_success() throws Exception {
        getGriffindor();
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + HARRY.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(HARRY.getId()))
                .andExpect(jsonPath("$.name").value(HARRY.getName()))
                .andExpect(jsonPath("$.age").value(HARRY.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.faculty.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.faculty.color").value(GRIFFINDOR.getColor()));
    }

    @Test
    void get_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void get_StudentNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + HARRY_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_STUDENT_NOT_FOUND));
    }

    @Test
    void getAll_success() throws Exception {
        when(studentService.getAll())
                .thenReturn(getStudents());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getStudents())));
    }

    @Test
    void getLastFiveStudents_success() throws Exception {
        when(studentService.getLastFiveStudents())
                .thenReturn(getStudents());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/last-five-students"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getStudents())));
    }

    @Test
    void getAmountAllStudents_success() throws Exception {
        when(studentService.getAmountAllStudents())
                .thenReturn(AMOUNT_STUDENTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/amount-all-students"))
                .andExpect(status().isOk())
                .andExpect(content().string(Integer.toString(AMOUNT_STUDENTS)));
    }

    @Test
    void getByAge_WithOnlyFirstParameter_success() throws Exception {
        when(studentService.getByAge(HARRY_AGE, null))
                .thenReturn(List.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + HARRY_AGE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HARRY))));
    }

    @Test
    void getByAge_WithOnlyFirstParameter_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void getByAge_WithOnlySecondParameter_success() throws Exception {
        when(studentService.getByAge(null, HERMIONE_AGE))
                .thenReturn(List.of(HERMIONE));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?maxAge=" + HERMIONE_AGE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HERMIONE))));
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?maxAge=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void getByAge_WithAllParameters_success() throws Exception {
        when(studentService.getByAge(HARRY_AGE, HERMIONE_AGE))
                .thenReturn(List.of(HARRY, HERMIONE));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + HARRY_AGE
                                + "&maxAge=" + HERMIONE_AGE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HARRY, HERMIONE))));
    }

    @Test
    void getByAge_WithAllParameters_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + HERMIONE_AGE
                                + "&maxAge=" + HARRY_AGE))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void getByAge_WithoutParameters_success() throws Exception {
        when(studentService.getByAge(null, null))
                .thenReturn(getStudents());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getStudents())));
    }

    @Test
    void getByName_success() throws Exception {
        when(studentService.getByName(HARRY_NAME))
                .thenReturn(List.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?name=" + HARRY_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HARRY))));
    }

    @Test
    void getByName_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?id=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void getByName_WithoutParameters_success() throws Exception {
        when(studentService.getByName(null))
                .thenReturn(getStudents());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getStudents())));
    }

    @Test
    void getAvgAgeStudents_success() throws Exception {
        DecimalFormat numberFormat = new DecimalFormat("#.#");

        when(studentRepository.getAvgAgeStudents())
                .thenReturn(AVG_AGE_STUDENTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/average-age-students"))
                .andExpect(status().isOk())
                .andExpect(content().string(numberFormat.format(AVG_AGE_STUDENTS)));
    }

    @Test
    void getFaculty_success() throws Exception {
        getGriffindor();
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?id=" + HARRY.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(GRIFFINDOR)));
    }

    @Test
    void edit_WithOnlyName_success() throws Exception {
        getGriffindor();
        getSlytherin();
        getHarry();

        Student expected = new Student(DRACO_NAME, HARRY_AGE, GRIFFINDOR);
        expected.setId(HARRY.getId());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + DRACO_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(expected.getFaculty().getId()))
                .andExpect(jsonPath("$.faculty.name").value(expected.getFaculty().getName()))
                .andExpect(jsonPath("$.faculty.color").value(expected.getFaculty().getColor()));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() throws Exception {
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + INVALIDE_NAME_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyNameAndAge_success() throws Exception {
        getGriffindor();
        getSlytherin();
        getHarry();

        Student expected = new Student(DRACO_NAME, DRACO_AGE, GRIFFINDOR);
        expected.setId(HARRY.getId());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + DRACO_NAME
                                + "&age=" + DRACO_AGE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(expected.getFaculty().getId()))
                .andExpect(jsonPath("$.faculty.name").value(expected.getFaculty().getName()))
                .andExpect(jsonPath("$.faculty.color").value(expected.getFaculty().getColor()));
    }

    @Test
    void edit_WithOnlyNameAndAge_InvalideInputException() throws Exception {
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_success() throws Exception {
        getGriffindor();
        getSlytherin();
        getHarry();

        Student expected = new Student(DRACO_NAME, HARRY_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + DRACO_NAME
                                + "&facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(expected.getFaculty().getId()))
                .andExpect(jsonPath("$.faculty.name").value(expected.getFaculty().getName()))
                .andExpect(jsonPath("$.faculty.color").value(expected.getFaculty().getColor()));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_InvalideInputException() throws Exception {
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyAge_success() throws Exception {
        getGriffindor();
        getSlytherin();
        getHarry();

        Student expected = new Student(HARRY_NAME, DRACO_AGE, GRIFFINDOR);
        expected.setId(HARRY.getId());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?age=" + DRACO_AGE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(expected.getFaculty().getId()))
                .andExpect(jsonPath("$.faculty.name").value(expected.getFaculty().getName()))
                .andExpect(jsonPath("$.faculty.color").value(expected.getFaculty().getColor()));
    }

    @Test
    void edit_WithOnlyAge_InvalideInputException() throws Exception {
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?age=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_success() throws Exception {
        getGriffindor();
        getSlytherin();
        getHarry();

        Student expected = new Student(HARRY_NAME, DRACO_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?age=" + DRACO_AGE
                                + "&facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(expected.getFaculty().getId()))
                .andExpect(jsonPath("$.faculty.name").value(expected.getFaculty().getName()))
                .andExpect(jsonPath("$.faculty.color").value(expected.getFaculty().getColor()));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_InvalideInputException() throws Exception {
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyFacultyId_success() throws Exception {
        getGriffindor();
        getSlytherin();
        getHarry();

        Student expected = new Student(HARRY_NAME, HARRY_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(expected.getFaculty().getId()))
                .andExpect(jsonPath("$.faculty.name").value(expected.getFaculty().getName()))
                .andExpect(jsonPath("$.faculty.color").value(expected.getFaculty().getColor()));
    }

    @Test
    void edit_WithOnlyFacultyId_InvalideInputException() throws Exception {
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithAllParameters_success() throws Exception {
        getGriffindor();
        getSlytherin();
        getHarry();

        Student expected = new Student(DRACO_NAME, DRACO_AGE, SLYTHERIN);
        expected.setId(HARRY.getId());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + DRACO_NAME
                                + "&age=" + DRACO_AGE
                                + "&facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.age").value(expected.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(expected.getFaculty().getId()))
                .andExpect(jsonPath("$.faculty.name").value(expected.getFaculty().getName()))
                .andExpect(jsonPath("$.faculty.color").value(expected.getFaculty().getColor()));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() throws Exception {
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithoutParameters_success() throws Exception {
        getGriffindor();
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(HARRY.getId()))
                .andExpect(jsonPath("$.name").value(HARRY.getName()))
                .andExpect(jsonPath("$.age").value(HARRY.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.faculty.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.faculty.color").value(GRIFFINDOR.getColor()));
    }

    @Test
    void remove_success() throws Exception {
        getGriffindor();
        getHarry();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + HARRY.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(HARRY.getId()))
                .andExpect(jsonPath("$.name").value(HARRY.getName()))
                .andExpect(jsonPath("$.age").value(HARRY.getAge()))
                .andExpect(jsonPath("$.faculty.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.faculty.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.faculty.color").value(GRIFFINDOR.getColor()));
    }
}