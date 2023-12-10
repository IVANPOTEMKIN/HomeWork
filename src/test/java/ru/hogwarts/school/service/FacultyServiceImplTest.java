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
        when(repository.findAll())
                .thenReturn(getFaculties());
        when(repository.save(any(Faculty.class)))
                .thenReturn(GRIFFINDOR);

        assertEquals(GRIFFINDOR,
                facultyService.add(GRIFFINDOR_NAME, GRIFFINDOR_COLOR));

        verify(repository, times(1))
                .findAll();
        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR);
        verify(checkService, times(1))
                .isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1))
                .save(GRIFFINDOR);
    }

    @Test
    void add_InvalideInputException() {
        when(repository.findAll())
                .thenReturn(getFaculties());
        when(repository.save(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.add(GRIFFINDOR_NAME, GRIFFINDOR_COLOR));

        verify(repository, times(1))
                .findAll();
        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR);
        verify(checkService, times(1))
                .isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1))
                .save(GRIFFINDOR);
    }

    @Test
    void add_FacultyAlreadyAddedException() {
        when(repository.findAll())
                .thenReturn(getFaculties());
        when(repository.save(any(Faculty.class)))
                .thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class,
                () -> facultyService.add(GRIFFINDOR_NAME, GRIFFINDOR_COLOR));

        verify(repository, times(1))
                .findAll();
        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR);
        verify(checkService, times(1))
                .isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1))
                .save(GRIFFINDOR);
    }

    @Test
    void get_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR, facultyService.get(GRIFFINDOR_ID));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.get(GRIFFINDOR_ID));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
    }

    @Test
    void get_FacultyNotFoundException() {
        when(repository.findById(any(Long.class)))
                .thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class,
                () -> facultyService.get(GRIFFINDOR_ID));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
    }

    @Test
    void edit_WithOnlyName_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(any(Faculty.class)))
                .thenReturn(new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR));

        assertEquals(new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR),
                facultyService.edit(GRIFFINDOR_ID, SLYTHERIN_NAME, null));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
        verify(repository, times(1))
                .save(new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(GRIFFINDOR_ID, SLYTHERIN_NAME, null));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
        verify(repository, times(1))
                .save(new Faculty(SLYTHERIN_NAME, GRIFFINDOR_COLOR));
    }

    @Test
    void edit_WithOnlyColor_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(any(Faculty.class)))
                .thenReturn(new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR));

        assertEquals(new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR),
                facultyService.edit(GRIFFINDOR_ID, null, SLYTHERIN_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
        verify(repository, times(1))
                .save(new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithOnlyColor_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(GRIFFINDOR_ID, null, SLYTHERIN_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
        verify(repository, times(1))
                .save(new Faculty(GRIFFINDOR_NAME, SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithAllParameters_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(any(Faculty.class)))
                .thenReturn(new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR));

        assertEquals(new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR),
                facultyService.edit(GRIFFINDOR_ID, SLYTHERIN_NAME, SLYTHERIN_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
        verify(repository, times(1))
                .save(new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(any(Faculty.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(GRIFFINDOR_ID, SLYTHERIN_NAME, SLYTHERIN_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
        verify(repository, times(1))
                .save(new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR));
    }

    @Test
    void edit_WithoutParameters_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR,
                facultyService.edit(GRIFFINDOR_ID, null, null));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
    }

    @Test
    void remove_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR, facultyService.remove(GRIFFINDOR_ID));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
        verify(repository, times(1))
                .delete(GRIFFINDOR);
    }

    @Test
    void getAll_success() {
        when(repository.findAll())
                .thenReturn(getFaculties());

        assertEquals(getFaculties(), facultyService.getAll());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void getByNameOrColor_WithOnlyName_success() {
        when(repository.findByNameContainsIgnoreCase(any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(GRIFFINDOR_NAME, null));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_NAME);
        verify(repository, times(1))
                .findByNameContainsIgnoreCase(GRIFFINDOR_NAME);
    }

    @Test
    void getByNameOrColor_WithOnlyName_InvalideInputException() {
        when(repository.findByNameContainsIgnoreCase(any(String.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(GRIFFINDOR_NAME, null));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_NAME);
        verify(repository, times(1))
                .findByNameContainsIgnoreCase(GRIFFINDOR_NAME);
    }

    @Test
    void getByNameOrColor_WithOnlyColor_success() {
        when(repository.findByColorContainsIgnoreCase(any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(null, GRIFFINDOR_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_COLOR);
        verify(repository, times(1))
                .findByColorContainsIgnoreCase(GRIFFINDOR_COLOR);
    }

    @Test
    void getByNameOrColor_WithOnlyColor_InvalideInputException() {
        when(repository.findByColorContainsIgnoreCase(any(String.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(null, GRIFFINDOR_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_COLOR);
        verify(repository, times(1))
                .findByColorContainsIgnoreCase(GRIFFINDOR_COLOR);
    }

    @Test
    void getByNameOrColor_WithAllParameters_success() {
        when(repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(any(String.class), any(String.class)))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(GRIFFINDOR_NAME, GRIFFINDOR_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_NAME, GRIFFINDOR_COLOR);
        verify(repository, times(1))
                .findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(GRIFFINDOR_NAME, GRIFFINDOR_COLOR);
    }

    @Test
    void getByNameOrColor_WithAllParameters_InvalideInputException() {
        when(repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(any(String.class), any(String.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(GRIFFINDOR_NAME, GRIFFINDOR_COLOR));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_NAME, GRIFFINDOR_COLOR);
        verify(repository, times(1))
                .findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(GRIFFINDOR_NAME, GRIFFINDOR_COLOR);
    }

    @Test
    void getByNameOrColor_WithoutParameters_success() {
        when(repository.findAll())
                .thenReturn(getFaculties());

        assertEquals(getFaculties(), facultyService.getByNameOrColor(null, null));

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void getStudentsByFacultyId_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR.getStudents(),
                facultyService.getStudentsByFacultyId(GRIFFINDOR_ID));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1))
                .findById(GRIFFINDOR_ID);
    }

    @Test
    void getAmountAllFaculties_success() {
        when(repository.getAmountAllFaculties())
                .thenReturn(AMOUNT_FACULTIES);

        assertEquals(AMOUNT_FACULTIES,
                facultyService.getAmountAllFaculties());

        verify(repository, times(1))
                .getAmountAllFaculties();
    }
}