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
import static org.mockito.Mockito.*;
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

    @Test
    void add_success() {
        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenReturn(false);
        when(repository.save(any(Faculty.class)))
                .thenReturn(GRIFFINDOR);

        assertEquals(GRIFFINDOR,
                facultyService.add(
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR));

        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(1)).save(any(Faculty.class));
    }

    @Test
    void add_InvalideInputException() {
        when(checkService.validateCheck(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.add(
                        INVALIDE_NAME_FACULTY,
                        INVALIDE_COLOR_FACULTY));

        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void add_FacultyAlreadyAddedException() {
        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class,
                () -> facultyService.add(
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR));

        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void get_success() {
        when(checkService.validateCheck(anyLong()))
                .thenReturn(false);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(GRIFFINDOR));

        GRIFFINDOR.setId(GRIFFINDOR_ID);

        assertEquals(GRIFFINDOR,
                facultyService.get(GRIFFINDOR.getId()));

        verify(checkService, times(1)).validateCheck(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void get_InvalideInputException() {
        when(checkService.validateCheck(anyLong()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.get(INVALIDE_ID));

        verify(checkService, times(1)).validateCheck(anyLong());
        verify(repository, times(0)).findById(anyLong());
    }

    @Test
    void get_FacultyNotFoundException() {
        when(checkService.validateCheck(anyLong()))
                .thenReturn(false);
        when(repository.findById(anyLong()))
                .thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class,
                () -> facultyService.get(GRIFFINDOR_ID));

        verify(checkService, times(1)).validateCheck(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void edit_WithOnlyName_success() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenReturn(false);

        Faculty expected = new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR);
        expected.setId(GRIFFINDOR.getId());

        when(repository.save(any(Faculty.class)))
                .thenReturn(expected);

        assertEquals(expected,
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        SLYTHERIN_NAME,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(1)).save(any(Faculty.class));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        INVALIDE_NAME_FACULTY,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void edit_WithOnlyName_FacultyAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        SLYTHERIN_NAME,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void edit_WithOnlyColor_success() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenReturn(false);

        Faculty expected = new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR);
        expected.setId(GRIFFINDOR.getId());

        when(repository.save(any(Faculty.class)))
                .thenReturn(expected);

        assertEquals(expected,
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        null,
                        SLYTHERIN_COLOR));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(1)).save(any(Faculty.class));
    }

    @Test
    void edit_WithOnlyColor_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        null,
                        INVALIDE_COLOR_FACULTY));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void edit_WithOnlyColor_FacultyAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        null,
                        SLYTHERIN_COLOR));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void edit_WithAllParameters_success() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenReturn(false);

        Faculty expected = new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR);

        when(repository.save(any(Faculty.class)))
                .thenReturn(expected);

        assertEquals(expected,
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(1)).save(any(Faculty.class));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        INVALIDE_NAME_FACULTY,
                        INVALIDE_COLOR_FACULTY));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void edit_WithAllParameters_FacultyAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Faculty.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isFacultyAlreadyAdded(anyCollection(), any(Faculty.class)))
                .thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class,
                () -> facultyService.edit(
                        GRIFFINDOR.getId(),
                        SLYTHERIN_NAME,
                        SLYTHERIN_COLOR));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Faculty.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isFacultyAlreadyAdded(anyCollection(), any(Faculty.class));
        verify(repository, times(0)).save(any(Faculty.class));
    }

    @Test
    void edit_WithoutParameters_success() {
        get_success();

        assertEquals(GRIFFINDOR,
                facultyService.edit(
                        GRIFFINDOR.getId(),
                        null,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
    }

    @Test
    void remove_success() {
        get_success();

        assertEquals(GRIFFINDOR,
                facultyService.remove(GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(1)).delete(any(Faculty.class));
    }

    @Test
    void getAll_success() {
        when(repository.findAll())
                .thenReturn(getFaculties());

        assertEquals(getFaculties(),
                facultyService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    void getByNameOrColor_WithOnlyName_success() {
        when(checkService.validateCheck(anyString()))
                .thenReturn(false);
        when(repository.findByNameContainsIgnoreCase(anyString()))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(
                        GRIFFINDOR_NAME,
                        null));

        verify(checkService, times(1)).validateCheck(anyString());
        verify(repository, times(1)).findByNameContainsIgnoreCase(anyString());
    }

    @Test
    void getByNameOrColor_WithOnlyName_InvalideInputException() {
        when(checkService.validateCheck(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(
                        INVALIDE_NAME_FACULTY,
                        null));

        verify(checkService, times(1)).validateCheck(anyString());
        verify(repository, times(0)).findByNameContainsIgnoreCase(anyString());
    }

    @Test
    void getByNameOrColor_WithOnlyColor_success() {
        when(checkService.validateCheck(anyString()))
                .thenReturn(false);
        when(repository.findByColorContainsIgnoreCase(anyString()))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(
                        null,
                        GRIFFINDOR_COLOR));

        verify(checkService, times(1)).validateCheck(anyString());
        verify(repository, times(1)).findByColorContainsIgnoreCase(anyString());
    }

    @Test
    void getByNameOrColor_WithOnlyColor_InvalideInputException() {
        when(checkService.validateCheck(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(
                        null,
                        INVALIDE_COLOR_FACULTY));

        verify(checkService, times(1)).validateCheck(anyString());
        verify(repository, times(0)).findByColorContainsIgnoreCase(anyString());
    }

    @Test
    void getByNameOrColor_WithAllParameters_success() {
        when(checkService.validateCheck(anyString(), anyString()))
                .thenReturn(false);
        when(repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(anyString(), anyString()))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(
                        GRIFFINDOR_NAME,
                        GRIFFINDOR_COLOR));

        verify(checkService, times(1)).validateCheck(anyString(), anyString());
        verify(repository, times(1)).findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(anyString(), anyString());
    }

    @Test
    void getByNameOrColor_WithAllParameters_InvalideInputException() {
        when(checkService.validateCheck(anyString(), anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(
                        INVALIDE_NAME_FACULTY,
                        INVALIDE_COLOR_FACULTY));

        verify(checkService, times(1)).validateCheck(anyString(), anyString());
        verify(repository, times(0)).findByColorContainsIgnoreCase(anyString());
    }

    @Test
    void getByNameOrColor_WithoutParameters_success() {
        getAll_success();

        assertEquals(getFaculties(),
                facultyService.getByNameOrColor(
                        null,
                        null));

        verify(repository, times(2)).findAll();
    }

    @Test
    void getStudentsByFacultyId_success() {
        get_success();

        HARRY.setId(HARRY_ID);
        GRIFFINDOR.setStudents(List.of(HARRY));

        assertEquals(List.of(HARRY),
                facultyService.getStudentsByFacultyId(GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
    }

    @Test
    void getAmountAllFaculties_success() {
        when(repository.getAmountAllFaculties())
                .thenReturn(AMOUNT_FACULTIES);

        assertEquals(AMOUNT_FACULTIES,
                facultyService.getAmountAllFaculties());

        verify(repository, times(1)).getAmountAllFaculties();
    }
}