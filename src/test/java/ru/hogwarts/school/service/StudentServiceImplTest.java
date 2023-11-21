package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.utils.Examples.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repository;
    @InjectMocks
    private StudentServiceImpl service;

    @BeforeEach
    public void setUp() {
        service.add(HARRY);
        service.add(RON);
        service.add(HERMIONE);
    }

    @Test
    void add_success() {
        assertTrue(getStudents().values().containsAll(service.getAll()));
    }

    @Test
    void get_success() {
        when(repository.get(HARRY.getId())).thenReturn(HARRY);
        assertEquals(HARRY, service.get(HARRY.getId()));
    }

    @Test
    void edit_success() {
        when(repository.edit(HARRY.getId(), DRACO)).thenReturn(EDIT_STUDENT);
        assertEquals(EDIT_STUDENT, service.edit(HARRY.getId(), DRACO));
    }

    @Test
    void remove_success() {
        when(repository.remove(HARRY.getId())).thenReturn(HARRY);
        assertEquals(HARRY, service.remove(HARRY.getId()));
    }

    @Test
    void getAll_success() {
        when(repository.getAll()).thenReturn(getStudents().values());
        assertEquals(getStudents().values(), service.getAll());
    }

    @Test
    void getByAge_success() {
        when(repository.getByAge(AGE)).thenReturn(getStudents().values());
        assertEquals(getStudents().values(), service.getByAge(AGE));
    }
}