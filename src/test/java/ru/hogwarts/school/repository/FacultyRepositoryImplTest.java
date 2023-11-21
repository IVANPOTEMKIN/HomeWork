package ru.hogwarts.school.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.repository.impl.FacultyRepositoryImpl;
import ru.hogwarts.school.service.CheckService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.utils.Examples.*;

@ExtendWith(MockitoExtension.class)
class FacultyRepositoryImplTest {

    @Mock
    private CheckService service;
    @InjectMocks
    private FacultyRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository.add(GRIFFINDOR);
        repository.add(SLYTHERIN);
        repository.add(RAVENCLAW);
    }

    @Test
    void add_success() {
        assertTrue(getFaculties().values().containsAll(repository.getAll()));
    }

    @Test
    void add_InvalideInputException() {
        when(service.validateCheck(INVALIDE_FACULTY)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> repository.add(INVALIDE_FACULTY));
    }

    @Test
    void add_StudentAlreadyAddedException() {
        when(service.isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR)).thenThrow(FacultyAlreadyAddedException.class);
        assertThrows(FacultyAlreadyAddedException.class, () -> repository.add(GRIFFINDOR));
    }

    @Test
    void get_success() {
        assertEquals(GRIFFINDOR, repository.get(GRIFFINDOR.getId()));
    }

    @Test
    void get_StudentNotFoundException() {
        when(service.isNotFacultyContains(getFaculties(), HUFFLEPUFF.getId())).thenThrow(FacultyNotFoundException.class);
        assertThrows(FacultyNotFoundException.class, () -> repository.get(HUFFLEPUFF.getId()));
    }

    @Test
    void edit_success() {
        assertEquals(EDIT_FACULTY, repository.edit(GRIFFINDOR.getId(), HUFFLEPUFF));
    }

    @Test
    void edit_InvalideInputException() {
        when(service.validateCheck(INVALIDE_FACULTY)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> repository.edit(GRIFFINDOR.getId(), INVALIDE_FACULTY));
    }

    @Test
    void remove_success() {
        assertEquals(GRIFFINDOR, repository.remove(GRIFFINDOR.getId()));
    }

    @Test
    void remove_StudentNotFoundException() {
        when(service.isNotFacultyContains(getFaculties(), HUFFLEPUFF.getId())).thenThrow(FacultyNotFoundException.class);
        assertThrows(FacultyNotFoundException.class, () -> repository.remove(HUFFLEPUFF.getId()));
    }

    @Test
    void getAll_success() {
        assertTrue(getFaculties().values().containsAll(repository.getAll()));
    }

    @Test
    void getByAge_success() {
        assertTrue(getFaculties().values().containsAll(repository.getByColor(COLOR)));
    }
}