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
        when(repository.save(HUFFLEPUFF)).thenReturn(HUFFLEPUFF);
        assertEquals(HUFFLEPUFF, facultyService.add(HUFFLEPUFF));
    }

    @Test
    void add_InvalideInputException() {
        when(repository.save(INVALIDE_FACULTY)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> facultyService.add(INVALIDE_FACULTY));
    }

    @Test
    void add_FacultyAlreadyAddedException() {
        when(repository.save(GRIFFINDOR)).thenThrow(FacultyAlreadyAddedException.class);
        assertThrows(FacultyAlreadyAddedException.class, () -> facultyService.add(GRIFFINDOR));
    }

    @Test
    void get_success() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));
        assertEquals(GRIFFINDOR, facultyService.get(GRIFFINDOR.getId()));
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(INVALIDE_FACULTY.getId())).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> facultyService.get(INVALIDE_FACULTY.getId()));
    }

    @Test
    void get_FacultyNotFoundException() {
        when(repository.findById(HUFFLEPUFF.getId())).thenThrow(FacultyNotFoundException.class);
        assertThrows(FacultyNotFoundException.class, () -> facultyService.get(HUFFLEPUFF.getId()));
    }

    @Test
    void edit_success() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));
        when(repository.save(EDIT_FACULTY)).thenReturn(EDIT_FACULTY);
        assertEquals(EDIT_FACULTY, facultyService.edit(GRIFFINDOR.getId(), HUFFLEPUFF));
    }

    @Test
    void edit_InvalideInputException() {
        when(repository.save(INVALIDE_FACULTY)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> facultyService.add(INVALIDE_FACULTY));
    }

    @Test
    void remove_success() {
        when(repository.findById(GRIFFINDOR.getId())).thenReturn(Optional.of(GRIFFINDOR));
        facultyService.remove(GRIFFINDOR.getId());
        verify(repository, times(1)).deleteById(GRIFFINDOR.getId());
    }

    @Test
    void getAll_success() {
        when(repository.findAll()).thenReturn(getFaculties());
        assertEquals(getFaculties(), facultyService.getAll());
    }

    @Test
    void getByColor_success() {
        when(repository.findByColor(GRIFFINDOR.getColor())).thenReturn(List.of(GRIFFINDOR));
        assertEquals(List.of(GRIFFINDOR), facultyService.getByColor(GRIFFINDOR.getColor()));
    }
}