package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.exception.InvalideInputException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.AvatarServiceImpl;
import ru.hogwarts.school.service.impl.CheckServiceImpl;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.utils.Examples.*;

@WebMvcTest
class StudentControllerTests {

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
    private CheckServiceImpl checkService;
    @SpyBean
    private AvatarServiceImpl avatarService;
    @InjectMocks
    private StudentController controller;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAdd_success() throws Exception {
        GRIFFINDOR.setId(GRIFFINDOR_ID);

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(HARRY);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student"
                                + "?name=" + HARRY_NAME
                                + "&age=" + HARRY_AGE
                                + "&facultyId=" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HARRY_NAME))
                .andExpect(jsonPath("$.age").value(HARRY_AGE));
    }

    @Test
    void testAdd_InvalideInputException() throws Exception {
        when(studentRepository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student"
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testAdd_StudentAlreadyAddedException() throws Exception {
        GRIFFINDOR.setId(GRIFFINDOR_ID);

        when(studentRepository.findAll())
                .thenReturn(getStudents());
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(HARRY);

        String expected = "Code: 400 BAD_REQUEST. Error: СТУДЕНТ УЖЕ ДОБАВЛЕН!";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student"
                                + "?name=" + HARRY_NAME
                                + "&age=" + HARRY_AGE
                                + "&facultyId=" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGet_success() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + HARRY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HARRY_NAME))
                .andExpect(jsonPath("$.age").value(HARRY_AGE));
    }

    @Test
    void testGet_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGet_StudentNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + HARRY_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_STUDENT_NOT_FOUND));
    }

    @Test
    void testGetAll_success() throws Exception {
        when(studentRepository.findAll())
                .thenReturn(getStudents());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getStudents())));
    }

    @Test
    void testGetByAge_WithOnlyFirstParameter_success() throws Exception {
        when(studentRepository.findByAge(any(Integer.class)))
                .thenReturn(List.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + HARRY_AGE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HARRY))));
    }

    @Test
    void testGetByAge_WithOnlyFirstParameter_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGetByAge_WithOnlySecondParameter_success() throws Exception {
        when(studentRepository.findByAge(any(Integer.class)))
                .thenReturn(List.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?maxAge=" + HARRY_AGE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HARRY))));
    }

    @Test
    void testGetByAge_WithOnlySecondParameter_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?maxAge=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGetByAge_WithAllParameters_success() throws Exception {
        when(studentRepository.findByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + HARRY_AGE
                                + "&maxAge=" + HARRY_AGE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HARRY))));
    }

    @Test
    void testGetByAge_WithAllParameters_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?minAge=" + INVALIDE_AGE_STUDENT
                                + "&maxAge=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGetByAge_WithoutParameters_success() throws Exception {
        when(studentRepository.findAll())
                .thenReturn(getStudents());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getStudents())));
    }

    @Test
    void testGetFacultyById_success() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        HARRY.setFaculty(GRIFFINDOR);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student"
                                + "?id=" + HARRY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GRIFFINDOR_NAME))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR_COLOR));
    }

    @Test
    void testEdit_WithOnlyName_success() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, HARRY_AGE, new Faculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(DRACO_NAME))
                .andExpect(jsonPath("$.age").value(HARRY_AGE));
    }

    @Test
    void testEdit_WithOnlyName_InvalideInputException() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testEdit_WithOnlyNameAndAge_success() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, DRACO_AGE, new Faculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME
                                + "&age=" + DRACO_AGE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(DRACO_NAME))
                .andExpect(jsonPath("$.age").value(DRACO_AGE));
    }

    @Test
    void testEdit_WithOnlyNameAndAge_InvalideInputException() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testEdit_WithOnlyNameAndFacultyId_success() throws Exception {
        SLYTHERIN.setId(SLYTHERIN_ID);

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(SLYTHERIN));
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, HARRY_AGE, DRACO.getFaculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME
                                + "&facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(DRACO_NAME))
                .andExpect(jsonPath("$.age").value(HARRY_AGE));
    }

    @Test
    void testEdit_WithOnlyNameAndFacultyId_InvalideInputException() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testEdit_WithOnlyAge_success() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, DRACO_AGE, new Faculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?age=" + DRACO_AGE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HARRY_NAME))
                .andExpect(jsonPath("$.age").value(DRACO_AGE));
    }

    @Test
    void testEdit_WithOnlyAge_InvalideInputException() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?age=" + INVALIDE_AGE_STUDENT))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testEdit_WithOnlyAgeAndFacultyId_success() throws Exception {
        SLYTHERIN.setId(SLYTHERIN_ID);

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(SLYTHERIN));
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, DRACO_AGE, DRACO.getFaculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?age=" + DRACO_AGE
                                + "&facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HARRY_NAME))
                .andExpect(jsonPath("$.age").value(DRACO_AGE));
    }

    @Test
    void testEdit_WithOnlyAgeAndFacultyId_InvalideInputException() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testEdit_WithOnlyFacultyId_success() throws Exception {
        SLYTHERIN.setId(SLYTHERIN_ID);

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(SLYTHERIN));
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, HARRY_AGE, DRACO.getFaculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HARRY_NAME))
                .andExpect(jsonPath("$.age").value(HARRY_AGE));
    }

    @Test
    void testEdit_WithOnlyFacultyId_InvalideInputException() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testEdit_WithAllParameters_success() throws Exception {
        SLYTHERIN.setId(SLYTHERIN_ID);

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(SLYTHERIN));
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, DRACO_AGE, DRACO.getFaculty()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + DRACO_NAME
                                + "&age=" + DRACO_AGE
                                + "&facultyId=" + SLYTHERIN_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(DRACO_NAME))
                .andExpect(jsonPath("$.age").value(DRACO_AGE));
    }

    @Test
    void testEdit_WithAllParameters_InvalideInputException() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID
                                + "?name=" + INVALIDE_NAME_STUDENT
                                + "&age=" + INVALIDE_AGE_STUDENT
                                + "&facultyId=" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testEdit_WithoutParameters_success() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/" + HARRY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HARRY_NAME))
                .andExpect(jsonPath("$.age").value(HARRY_AGE));
    }

    @Test
    void testRemove_success() throws Exception {
        when(studentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + HARRY_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(HARRY_NAME))
                .andExpect(jsonPath("$.age").value(HARRY_AGE));
    }
}