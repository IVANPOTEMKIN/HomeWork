package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.utils.Examples.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceImplTest {

    @Mock
    private FacultyRepository repository;
    @InjectMocks
    private FacultyServiceImpl service;

    @BeforeEach
    public void setUp() {
        service.add(GRIFFINDOR);
        service.add(SLYTHERIN);
        service.add(RAVENCLAW);
    }

    @Test
    void add_success() {
        assertTrue(getFaculties().values().containsAll(service.getAll()));
    }

    @Test
    void get_success() {
        when(repository.get(GRIFFINDOR.getId())).thenReturn(GRIFFINDOR);
        assertEquals(GRIFFINDOR, service.get(GRIFFINDOR.getId()));
    }

    @Test
    void edit_success() {
        when(repository.edit(GRIFFINDOR.getId(), HUFFLEPUFF)).thenReturn(EDIT_FACULTY);
        assertEquals(EDIT_FACULTY, service.edit(GRIFFINDOR.getId(), HUFFLEPUFF));
    }

    @Test
    void remove_success() {
        when(repository.remove(GRIFFINDOR.getId())).thenReturn(GRIFFINDOR);
        assertEquals(GRIFFINDOR, service.remove(GRIFFINDOR.getId()));
    }

    @Test
    void getAll_success() {
        when(repository.getAll()).thenReturn(getFaculties().values());
        assertEquals(getFaculties().values(), service.getAll());
    }

    @Test
    void getByAge_success() {
        when(repository.getByColor(COLOR)).thenReturn(getFaculties().values());
        assertEquals(getFaculties().values(), service.getByColor(COLOR));
    }
}