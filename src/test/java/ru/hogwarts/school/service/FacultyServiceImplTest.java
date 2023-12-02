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
        when(repository.findAll()).thenReturn(getFaculties());
        when(repository.save(GRIFFINDOR)).thenReturn(GRIFFINDOR);

        assertEquals(GRIFFINDOR, facultyService.add(GRIFFINDOR.getName(), GRIFFINDOR.getColor()));

        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).validateCheck(GRIFFINDOR);
        verify(checkService, times(1)).isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1)).save(GRIFFINDOR);
    }

    @Test
    void add_InvalideInputException() {
        when(repository.findAll()).thenReturn(getFaculties());
        when(repository.save(GRIFFINDOR)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.add(GRIFFINDOR.getName(), GRIFFINDOR.getColor()));

        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).validateCheck(GRIFFINDOR);
        verify(checkService, times(1)).isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1)).save(GRIFFINDOR);
    }

    @Test
    void add_FacultyAlreadyAddedException() {
        when(repository.findAll()).thenReturn(getFaculties());
        when(repository.save(GRIFFINDOR)).thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class, () -> facultyService.add(GRIFFINDOR.getName(), GRIFFINDOR.getColor()));

        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).validateCheck(GRIFFINDOR);
        verify(checkService, times(1)).isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1)).save(GRIFFINDOR);
    }

    @Test
    void get_success() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR, facultyService.get(GRIFFINDOR_ID));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(GRIFFINDOR_ID)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.get(GRIFFINDOR_ID));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
    }

    @Test
    void get_FacultyNotFoundException() {
        when(repository.findById(GRIFFINDOR_ID)).thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class, () -> facultyService.get(GRIFFINDOR_ID));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
    }

    @Test
    void edit_WithOnlyName_success() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(new Faculty(HUFFLEPUFF.getName(), GRIFFINDOR.getColor())))
                .thenReturn(new Faculty(HUFFLEPUFF.getName(), GRIFFINDOR.getColor()));

        assertEquals(new Faculty(HUFFLEPUFF.getName(), GRIFFINDOR.getColor()),
                facultyService.edit(GRIFFINDOR_ID, HUFFLEPUFF.getName(), null));

        verify(checkService, times(1)).validateCheck(HUFFLEPUFF.getName());
        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
        verify(repository, times(1)).save(new Faculty(HUFFLEPUFF.getName(), GRIFFINDOR.getColor()));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(new Faculty(HUFFLEPUFF.getName(), GRIFFINDOR.getColor())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(GRIFFINDOR_ID, HUFFLEPUFF.getName(), null));

        verify(checkService, times(1)).validateCheck(HUFFLEPUFF.getName());
        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
        verify(repository, times(1)).save(new Faculty(HUFFLEPUFF.getName(), GRIFFINDOR.getColor()));
    }

    @Test
    void edit_WithOnlyColor_success() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(new Faculty(GRIFFINDOR.getName(), HUFFLEPUFF.getColor())))
                .thenReturn(new Faculty(GRIFFINDOR.getName(), HUFFLEPUFF.getColor()));

        assertEquals(new Faculty(GRIFFINDOR.getName(), HUFFLEPUFF.getColor()),
                facultyService.edit(GRIFFINDOR_ID, null, HUFFLEPUFF.getColor()));

        verify(checkService, times(1)).validateCheck(HUFFLEPUFF.getColor());
        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
        verify(repository, times(1)).save(new Faculty(GRIFFINDOR.getName(), HUFFLEPUFF.getColor()));
    }

    @Test
    void edit_WithOnlyColor_InvalideInputException() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(new Faculty(GRIFFINDOR.getName(), HUFFLEPUFF.getColor())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.edit(GRIFFINDOR_ID, null, HUFFLEPUFF.getColor()));

        verify(checkService, times(1)).validateCheck(HUFFLEPUFF.getColor());
        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
        verify(repository, times(1)).save(new Faculty(GRIFFINDOR.getName(), HUFFLEPUFF.getColor()));
    }

    @Test
    void edit_WithAllParameters_success() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(EDIT_FACULTY)).thenReturn(EDIT_FACULTY);

        assertEquals(EDIT_FACULTY, facultyService.edit(GRIFFINDOR_ID, HUFFLEPUFF.getName(), HUFFLEPUFF.getColor()));

        verify(checkService, times(1)).validateCheck(HUFFLEPUFF.getName(), HUFFLEPUFF.getColor());
        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
        verify(repository, times(1)).save(EDIT_FACULTY);
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(EDIT_FACULTY)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.edit(GRIFFINDOR_ID, HUFFLEPUFF.getName(), HUFFLEPUFF.getColor()));

        verify(checkService, times(1)).validateCheck(HUFFLEPUFF.getName(), HUFFLEPUFF.getColor());
        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
        verify(repository, times(1)).save(EDIT_FACULTY);
    }

    @Test
    void edit_WithoutParameters_success() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR, facultyService.edit(GRIFFINDOR_ID, null, null));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
    }

    @Test
    void remove_success() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR, facultyService.remove(GRIFFINDOR_ID));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
        verify(repository, times(1)).delete(GRIFFINDOR);
    }

    @Test
    void getAll_success() {
        when(repository.findAll()).thenReturn(getFaculties());

        assertEquals(getFaculties(), facultyService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    void getByNameOrColor_WithOnlyName_success() {
        when(repository.findByNameContainsIgnoreCase(GRIFFINDOR.getName()))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(GRIFFINDOR.getName(), null));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR.getName());
        verify(repository, times(1))
                .findByNameContainsIgnoreCase(GRIFFINDOR.getName());
    }

    @Test
    void getByNameOrColor_WithOnlyName_InvalideInputException() {
        when(repository.findByNameContainsIgnoreCase(GRIFFINDOR.getName()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(GRIFFINDOR.getName(), null));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR.getName());
        verify(repository, times(1))
                .findByNameContainsIgnoreCase(GRIFFINDOR.getName());
    }

    @Test
    void getByNameOrColor_WithOnlyColor_success() {
        when(repository.findByColorContainsIgnoreCase(GRIFFINDOR.getColor()))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(null, GRIFFINDOR.getColor()));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR.getColor());
        verify(repository, times(1))
                .findByColorContainsIgnoreCase(GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithOnlyColor_InvalideInputException() {
        when(repository.findByColorContainsIgnoreCase(GRIFFINDOR.getColor()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(null, GRIFFINDOR.getColor()));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR.getColor());
        verify(repository, times(1))
                .findByColorContainsIgnoreCase(GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithAllParameters_success() {
        when(repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor()))
                .thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR),
                facultyService.getByNameOrColor(GRIFFINDOR.getName(), GRIFFINDOR.getColor()));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
        verify(repository, times(1))
                .findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithAllParameters_InvalideInputException() {
        when(repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> facultyService.getByNameOrColor(GRIFFINDOR.getName(), GRIFFINDOR.getColor()));

        verify(checkService, times(1))
                .validateCheck(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
        verify(repository, times(1))
                .findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithoutParameters_success() {
        when(repository.findAll()).thenReturn(getFaculties());

        assertEquals(getFaculties(), facultyService.getByNameOrColor(null, null));

        verify(repository, times(1)).findAll();
    }

    @Test
    void getStudentsByFacultyId_success() {
        when(repository.findById(GRIFFINDOR_ID)).thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR.getStudents(), facultyService.getStudentsByFacultyId(GRIFFINDOR_ID));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR_ID);
        verify(repository, times(1)).findById(GRIFFINDOR_ID);
    }
}