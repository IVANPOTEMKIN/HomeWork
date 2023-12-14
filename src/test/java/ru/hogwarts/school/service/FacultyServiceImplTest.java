package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.FacultyAlreadyAddedException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.InvalideInputException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.utils.Examples.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceImplTest {

    @Mock
    private FacultyRepository repository;
    @Mock
    private CheckService checkService;
    @InjectMocks
    private FacultyServiceImpl facultyService;

    @BeforeEach
    public void setUp() {
        facultyService = new FacultyServiceImpl(repository, checkService);
    }

    private void getGriffindor() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        GRIFFINDOR.setId(GRIFFINDOR_ID);
    }

    @Test
    void add_success() {
        when(repository.save(any(Faculty.class)))
                .thenReturn(GRIFFINDOR);

        assertEquals(GRIFFINDOR,
                facultyService.add(
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR));
    }

    @Test
    void add_InvalideInputException() {
        when(checkService.validateCheck(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.add(
                        INVALIDE_NAME_FACULTY,
                        INVALIDE_COLOR_FACULTY));
    }

    @Test
    void add_FacultyAlreadyAddedException() {
        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class,
                () -> facultyService.add(
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR));
    }

    @Test
    void get_success() {
        getGriffindor();

        assertEquals(GRIFFINDOR,
                facultyService.get(GRIFFINDOR_ID));
    }

    @Test
    void get_InvalideInputException() {
        when(checkService.validateCheck(any(Long.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.get(INVALIDE_ID));
    }

    @Test
    void get_FacultyNotFoundException() {
        when(repository.findById(any(Long.class)))
                .thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class,
                () -> facultyService.get(GRIFFINDOR_ID));
    }

    @Test
    void edit_WithOnlyName_success() {
        getGriffindor();

        when(repository.save(any(Faculty.class)))
                .thenReturn(new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR));

        assertEquals(new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR),
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        SLYTHERIN_NAME,
                        null));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        getGriffindor();

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        INVALIDE_NAME_FACULTY,
                        null));
    }

    @Test
    void edit_WithOnlyColor_success() {
        getGriffindor();

        when(repository.save(any(Faculty.class)))
                .thenReturn(new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR));

        assertEquals(new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR),
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        null,
                        SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithOnlyColor_InvalideInputException() {
        getGriffindor();

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        null,
                        INVALIDE_COLOR_FACULTY));
    }

    @Test
    void edit_WithAllParameters_success() {
        getGriffindor();

        when(repository.save(any(Faculty.class)))
                .thenReturn(new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR));

        assertEquals(new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR),
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        getGriffindor();

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        INVALIDE_NAME_FACULTY,
                        INVALIDE_COLOR_FACULTY));
    }

    @Test
    void edit_WithoutParameters_success() {
        getGriffindor();

        assertEquals(GRIFFINDOR,
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        null,
                        null));
    }

    @Test
    void remove_success() {
        getGriffindor();

        assertEquals(GRIFFINDOR,
                facultyService.remove(GRIFFINDOR.getId()));
    }

    @Test
    void getAll_success() {
        when(repository.findAll())
                .thenReturn(getFaculties());

        assertEquals(getFaculties(),
                facultyService.getAll());
    }

    @Test
    void getByNameOrColor_WithOnlyName_success() {
        when(repository.findByNameContainsIgnoreCase(any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(
                        GRIFFINDOR_NAME,
                        null));
    }

    @Test
    void getByNameOrColor_WithOnlyName_InvalideInputException() {
        when(checkService.validateCheck(any(String.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(
                        INVALIDE_NAME_FACULTY,
                        null));
    }

    @Test
    void getByNameOrColor_WithOnlyColor_success() {
        when(repository.findByColorContainsIgnoreCase(any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(
                        null,
                        GRIFFINDOR_COLOR));
    }

    @Test
    void getByNameOrColor_WithOnlyColor_InvalideInputException() {
        when(checkService.validateCheck(any(String.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(
                        null,
                        INVALIDE_COLOR_FACULTY));
    }

    @Test
    void getByNameOrColor_WithAllParameters_success() {
        when(repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(any(String.class), any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR));
    }

    @Test
    void getByNameOrColor_WithAllParameters_InvalideInputException() {
        when(checkService.validateCheck(any(String.class), any(String.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(
                        INVALIDE_NAME_FACULTY,
                        INVALIDE_COLOR_FACULTY));
    }

    @Test
    void getByNameOrColor_WithoutParameters_success() {
        when(repository.findAll())
                .thenReturn(getFaculties());

        assertEquals(getFaculties(),
                facultyService.getByNameOrColor(
                        null,
                        null));
    }

    @Test
    void getStudentsByFacultyId_success() {
        getGriffindor();

        assertEquals(GRIFFINDOR.getStudents(),
                facultyService.getStudentsByFacultyId(GRIFFINDOR.getId()));
    }

    @Test
    void getAmountAllFaculties_success() {
        when(repository.getAmountAllFaculties())
                .thenReturn(AMOUNT_FACULTIES);

        assertEquals(AMOUNT_FACULTIES,
                facultyService.getAmountAllFaculties());
    }
}