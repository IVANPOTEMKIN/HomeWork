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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.utils.Examples.*;

@WebMvcTest
class FacultyControllerTest {

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
    @SpyBean
    private AdditionalServiceImpl additionalService;
    @InjectMocks
    private FacultyController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void add_success() throws Exception {
        when(facultyService.add(GRIFFINDOR_NAME, GRIFFINDOR_COLOR))
                .thenReturn(GRIFFINDOR);
        GRIFFINDOR.setId(GRIFFINDOR_ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty"
                                + "?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR.getColor()));
    }

    @Test
    void add_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty"
                                + "?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void add_FacultyAlreadyAddedException() throws Exception {
        add_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty"
                                + "?name=" + GRIFFINDOR.getName()
                                + "&color=" + GRIFFINDOR.getColor()))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_FACULTY_ALREADY_ADDED));
    }

    @Test
    void get_success() throws Exception {
        when(facultyRepository.findById(anyLong()))
                .thenReturn(Optional.of(GRIFFINDOR));
        GRIFFINDOR.setId(GRIFFINDOR_ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + GRIFFINDOR.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR.getColor()));
    }

    @Test
    void get_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + INVALIDE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void get_FacultyNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + GRIFFINDOR_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_FACULTY_NOT_FOUND));
    }

    @Test
    void getAll_success() throws Exception {
        when(facultyService.getAll())
                .thenReturn(getFaculties());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getFaculties())));
    }

    @Test
    void getAmountAllFaculties_success() throws Exception {
        when(facultyService.getAmountAllFaculties())
                .thenReturn(AMOUNT_FACULTIES);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/amount-all-faculties"))
                .andExpect(status().isOk())
                .andExpect(content().string(Integer.toString(AMOUNT_FACULTIES)));
    }

    @Test
    void getByNameOrColor_WithOnlyName_success() throws Exception {
        when(facultyService.getByNameOrColor(GRIFFINDOR_NAME, null))
                .thenReturn(List.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + GRIFFINDOR_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(GRIFFINDOR))));
    }

    @Test
    void getByNameOrColor_WithOnlyName_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + INVALIDE_NAME_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void getByNameOrColor_WithOnlyColor_success() throws Exception {
        when(facultyService.getByNameOrColor(null, GRIFFINDOR_COLOR))
                .thenReturn(List.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?color=" + GRIFFINDOR_COLOR))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(GRIFFINDOR))));
    }

    @Test
    void getByNameOrColor_WithOnlyColor_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void getByNameOrColor_WithAllParameters_success() throws Exception {
        when(facultyService.getByNameOrColor(GRIFFINDOR_NAME, GRIFFINDOR_COLOR))
                .thenReturn(List.of(GRIFFINDOR));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + GRIFFINDOR_NAME
                                + "&color=" + GRIFFINDOR_COLOR))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(GRIFFINDOR))));
    }

    @Test
    void getByNameOrColor_WithAllParameters_InvalideInputException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void getByNameOrColor_WithoutParameters_success() throws Exception {
        when(facultyService.getByNameOrColor(null, null))
                .thenReturn(getFaculties());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getFaculties())));
    }

    @Test
    void getLongestName_success() throws Exception {
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/longest-name"))
                .andExpect(status().isOk())
                .andExpect(content().string(GRIFFINDOR_NAME.toUpperCase()));
    }

    @Test
    void getStudents_success() throws Exception {
        get_success();

        GRIFFINDOR.setStudents(List.of(HARRY));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"
                                + "?id=" + GRIFFINDOR.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(HARRY))));
    }

    @Test
    void edit_WithOnlyName_success() throws Exception {
        get_success();

        Faculty expected = new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR);
        expected.setId(GRIFFINDOR.getId());

        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?name=" + SLYTHERIN_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() throws Exception {
        get_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?name=" + INVALIDE_NAME_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyName_FacultyAlreadyAddedException() throws Exception {
        get_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?name=" + GRIFFINDOR.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_FACULTY_ALREADY_ADDED));
    }

    @Test
    void edit_WithOnlyColor_success() throws Exception {
        get_success();

        Faculty expected = new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR);
        expected.setId(GRIFFINDOR.getId());

        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?color=" + SLYTHERIN_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));
    }

    @Test
    void edit_WithOnlyColor_InvalideInputException() throws Exception {
        get_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithOnlyColor_FacultyAlreadyAddedException() throws Exception {
        get_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?color=" + GRIFFINDOR.getColor()))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_FACULTY_ALREADY_ADDED));
    }

    @Test
    void edit_WithAllParameters_success() throws Exception {
        get_success();

        Faculty expected = new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR);
        expected.setId(GRIFFINDOR.getId());

        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?name=" + SLYTHERIN_NAME
                                + "&color=" + SLYTHERIN_COLOR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.color").value(expected.getColor()));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() throws Exception {
        get_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?name=" + INVALIDE_NAME_FACULTY
                                + "&color=" + INVALIDE_COLOR_FACULTY))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_INVALIDE_DATES));
    }

    @Test
    void edit_WithAllParameters_FacultyAlreadyAddedException() throws Exception {
        get_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()
                                + "?name=" + GRIFFINDOR.getName()
                                + "&color=" + GRIFFINDOR.getColor()))
                .andExpect(status().isOk())
                .andExpect(content().string(MESSAGE_FACULTY_ALREADY_ADDED));
    }

    @Test
    void edit_WithoutParameters_success() throws Exception {
        get_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + GRIFFINDOR.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR.getColor()));
    }

    @Test
    void remove_success() throws Exception {
        get_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + GRIFFINDOR.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GRIFFINDOR.getId()))
                .andExpect(jsonPath("$.name").value(GRIFFINDOR.getName()))
                .andExpect(jsonPath("$.color").value(GRIFFINDOR.getColor()));
    }
}