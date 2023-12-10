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
import ru.hogwarts.school.model.Faculty;
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
class FacultyControllerTests {

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
    private FacultyController controller;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAdd_success() throws Exception {
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(GRIFFINDOR);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty"
                                + "?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GRIFFINDOR_NAME))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR_COLOR));
    }

    @Test
    void testAdd_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty"
                                + "?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testAdd_FacultyAlreadyAddedException() throws Exception {
        when(facultyRepository.findAll())
                .thenReturn(getFaculties());

        String expected = "Code: 400 BAD_REQUEST. Error: ФАКУЛЬТЕТ УЖЕ ДОБАВЛЕН!";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty"
                                + "?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void testGet_success() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GRIFFINDOR_NAME))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR_COLOR));
    }

    @Test
    void testGet_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGet_FacultyNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_FACULTY_NOT_FOUND));
    }

    @Test
    void testGetAll_success() throws Exception {
        when(facultyRepository.findAll())
                .thenReturn(getFaculties());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getFaculties())));
    }

    @Test
    void testGetAmountAllFaculties_success() throws Exception {
        when(facultyRepository.getAmountAllFaculties())
                .thenReturn(AMOUNT_FACULTIES);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/amount-all-faculties"))
                .andExpect(status().isOk())
                .andExpect(content().string(Integer.toString(AMOUNT_FACULTIES)));
    }

    @Test
    void testGetByNameOrColor_WithOnlyName_success() throws Exception {
        when(facultyRepository.findByNameContainsIgnoreCase(any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + GRIFFINDOR_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(GRIFFINDOR))));
    }

    @Test
    void testGetByNameOrColor_WithOnlyName_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + INVALIDE_NAME_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGetByNameOrColor_WithOnlyColor_success() throws Exception {
        when(facultyRepository.findByColorContainsIgnoreCase(any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?color=" + GRIFFINDOR_COLOR))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(GRIFFINDOR))));
    }

    @Test
    void testGetByNameOrColor_WithOnlyColor_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGetByNameOrColor_WithAllParameters_success() throws Exception {
        when(facultyRepository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(any(String.class), any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(GRIFFINDOR))));
    }

    @Test
    void testGetByNameOrColor_WithAllParameters_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void testGetByNameOrColor_WithoutParameters_success() throws Exception {
        when(facultyRepository.findAll())
                .thenReturn(getFaculties());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getFaculties())));
    }

    @Test
    void testGetStudentsByFacultyId_success() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        GRIFFINDOR.setStudents(List.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?id=" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(GRIFFINDOR.getStudents())));
    }

    @Test
    void edit_WithOnlyName_success() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR_ID
                                + "?name=" + SLYTHERIN_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(SLYTHERIN_NAME))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR_COLOR));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR_ID
                                + "?name=" + INVALIDE_NAME_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyColor_success() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR_ID
                                + "?color=" + SLYTHERIN_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GRIFFINDOR_NAME))
                .andExpect(jsonPath("$.color").value(SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithOnlyColor_InvalideInputException() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR_ID
                                + "?color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithAllParameters_success() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR_ID
                                + "?name=" + SLYTHERIN_NAME
                                + "&color=" + SLYTHERIN_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(SLYTHERIN_NAME))
                .andExpect(jsonPath("$.color").value(SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR_ID
                                + "?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithoutParameters_success() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GRIFFINDOR_NAME))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR_COLOR));
    }

    @Test
    void remove_success() throws Exception {
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GRIFFINDOR_NAME))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR_COLOR));
    }
}