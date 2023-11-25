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

        assertEquals(GRIFFINDOR, facultyService.add(GRIFFINDOR));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR);
        verify(checkService, times(1)).isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1)).save(GRIFFINDOR);
    }

    @Test
    void add_InvalideInputException() {
        when(repository.findAll()).thenReturn(getFaculties());
        when(repository.save(GRIFFINDOR)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.add(GRIFFINDOR));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR);
        verify(checkService, times(1)).isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1)).save(GRIFFINDOR);
    }

    @Test
    void add_FacultyAlreadyAddedException() {
        when(repository.findAll()).thenReturn(getFaculties());
        when(repository.save(GRIFFINDOR)).thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class, () -> facultyService.add(GRIFFINDOR));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR);
        verify(checkService, times(1)).isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR);
        verify(repository, times(1)).save(GRIFFINDOR);
    }

    @Test
    void get_success() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR, facultyService.get(GRIFFINDOR.getId()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getId());
        verify(repository, times(1)).findById(GRIFFINDOR.getId());
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(GRIFFINDOR.getId())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.get(GRIFFINDOR.getId()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getId());
        verify(repository, times(1)).findById(GRIFFINDOR.getId());
    }

    @Test
    void get_FacultyNotFoundException() {
        when(repository.findById(GRIFFINDOR.getId())).thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class, () -> facultyService.get(GRIFFINDOR.getId()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getId());
        verify(repository, times(1)).findById(GRIFFINDOR.getId());
    }

    @Test
    void edit_success() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(EDIT_FACULTY)).thenReturn(EDIT_FACULTY);

        assertEquals(EDIT_FACULTY, facultyService.edit(GRIFFINDOR.getId(), HUFFLEPUFF));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getId());
        verify(checkService, times(1)).validateCheck(HUFFLEPUFF);
        verify(repository, times(1)).findById(GRIFFINDOR.getId());
    }

    @Test
    void edit_InvalideInputException() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(EDIT_FACULTY)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.edit(GRIFFINDOR.getId(), HUFFLEPUFF));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getId());
        verify(checkService, times(1)).validateCheck(HUFFLEPUFF);
        verify(repository, times(1)).findById(GRIFFINDOR.getId());
    }

    @Test
    void remove_success() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR, facultyService.remove(GRIFFINDOR.getId()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getId());
        verify(repository, times(1)).findById(GRIFFINDOR.getId());
    }

    @Test
    void getAll_success() {
        when(repository.findAll()).thenReturn(getFaculties());

        assertEquals(getFaculties(), facultyService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    void getByNameOrColor_WithOnlyName_success() {
        when(repository.findByNameIgnoreCase(GRIFFINDOR.getName())).thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR), facultyService.getByNameOrColor(GRIFFINDOR.getName(), null));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getName());
        verify(repository, times(1)).findByNameIgnoreCase(GRIFFINDOR.getName());
    }

    @Test
    void getByNameOrColor_WithOnlyName_InvalideInputException() {
        when(repository.findByNameIgnoreCase(GRIFFINDOR.getName())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.getByNameOrColor(GRIFFINDOR.getName(), null));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getName());
        verify(repository, times(1)).findByNameIgnoreCase(GRIFFINDOR.getName());
    }

    @Test
    void getByNameOrColor_WithOnlyColor_success() {
        when(repository.findByColorIgnoreCase(GRIFFINDOR.getColor())).thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR), facultyService.getByNameOrColor(null, GRIFFINDOR.getColor()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getColor());
        verify(repository, times(1)).findByColorIgnoreCase(GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithOnlyColor_InvalideInputException() {
        when(repository.findByColorIgnoreCase(GRIFFINDOR.getColor())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.getByNameOrColor(null, GRIFFINDOR.getColor()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getColor());
        verify(repository, times(1)).findByColorIgnoreCase(GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithAllParameters_success() {
        when(repository.findByNameIgnoreCaseAndColorIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor())).thenReturn(List.of(GRIFFINDOR));

        assertEquals(List.of(GRIFFINDOR), facultyService.getByNameOrColor(GRIFFINDOR.getName(), GRIFFINDOR.getColor()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
        verify(repository, times(1)).findByNameIgnoreCaseAndColorIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithAllParameters_InvalideInputException() {
        when(repository.findByNameIgnoreCaseAndColorIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> facultyService.getByNameOrColor(GRIFFINDOR.getName(), GRIFFINDOR.getColor()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
        verify(repository, times(1)).findByNameIgnoreCaseAndColorIgnoreCase(GRIFFINDOR.getName(), GRIFFINDOR.getColor());
    }

    @Test
    void getByNameOrColor_WithoutParameters_success() {
        when(repository.findAll()).thenReturn(getFaculties());

        assertEquals(getFaculties(), facultyService.getByNameOrColor(null, null));

        verify(repository, times(1)).findAll();
    }

    @Test
    void getStudentsByFacultyId_success() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));

        assertEquals(GRIFFINDOR.getStudents(), facultyService.getStudentsByFacultyId(GRIFFINDOR.getId()));

        verify(checkService, times(1)).validateCheck(GRIFFINDOR.getId());
        verify(repository, times(1)).findById(GRIFFINDOR.getId());
    }
}