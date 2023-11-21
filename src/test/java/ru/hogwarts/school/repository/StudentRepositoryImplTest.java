package ru.hogwarts.school.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.repository.impl.StudentRepositoryImpl;
import ru.hogwarts.school.service.CheckService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.utils.Examples.*;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryImplTest {

    @Mock
    private CheckService service;
    @InjectMocks
    private StudentRepositoryImpl repository;

    @BeforeEach
    public void setUp() {
        repository.add(HARRY);
        repository.add(RON);
        repository.add(HERMIONE);
    }

    @Test
    void add_success() {
        assertTrue(getStudents().values().containsAll(repository.getAll()));
    }

    @Test
    void add_InvalideInputException() {
        when(service.validateCheck(INVALIDE_STUDENT)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> repository.add(INVALIDE_STUDENT));
    }

    @Test
    void add_StudentAlreadyAddedException() {
        when(service.isStudentAlreadyAdded(getStudents(), HARRY)).thenThrow(StudentAlreadyAddedException.class);
        assertThrows(StudentAlreadyAddedException.class, () -> repository.add(HARRY));
    }

    @Test
    void get_success() {
        assertEquals(HARRY, repository.get(HARRY.getId()));
    }

    @Test
    void get_StudentNotFoundException() {
        when(service.isNotStudentContains(getStudents(), DRACO.getId())).thenThrow(StudentNotFoundException.class);
        assertThrows(StudentNotFoundException.class, () -> repository.get(DRACO.getId()));
    }

    @Test
    void edit_success() {
        assertEquals(EDIT_STUDENT, repository.edit(HARRY.getId(), DRACO));
    }

    @Test
    void edit_InvalideInputException() {
        when(service.validateCheck(INVALIDE_STUDENT)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> repository.edit(HARRY.getId(), INVALIDE_STUDENT));
    }

    @Test
    void remove_success() {
        assertEquals(HARRY, repository.remove(HARRY.getId()));
    }

    @Test
    void remove_StudentNotFoundException() {
        when(service.isNotStudentContains(getStudents(), DRACO.getId())).thenThrow(StudentNotFoundException.class);
        assertThrows(StudentNotFoundException.class, () -> repository.remove(DRACO.getId()));
    }

    @Test
    void getAll_success() {
        assertTrue(getStudents().values().containsAll(repository.getAll()));
    }

    @Test
    void getByAge_success() {
        assertTrue(getStudents().values().containsAll(repository.getByAge(AGE)));
    }
}