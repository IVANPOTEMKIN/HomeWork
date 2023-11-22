package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.InvalideInputException;
import ru.hogwarts.school.exception.StudentAlreadyAddedException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.hogwarts.school.utils.Examples.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repository;
    @Mock
    private CheckService checkService;
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    public void setUp() {
        studentService = new StudentServiceImpl(repository, checkService);
        studentService.add(HARRY);
        studentService.add(RON);
        studentService.add(HERMIONE);
    }

    @Test
    void add_success() {
        when(repository.save(DRACO)).thenReturn(DRACO);
        assertEquals(DRACO, studentService.add(DRACO));
    }

    @Test
    void add_InvalideInputException() {
        when(checkService.validateCheck(INVALIDE_STUDENT)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> studentService.add(INVALIDE_STUDENT));
    }

    @Test
    void add_StudentAlreadyAddedException() {
        when(checkService.isStudentAlreadyAdded(getStudents(), HARRY)).thenThrow(StudentAlreadyAddedException.class);
        assertThrows(StudentAlreadyAddedException.class, () -> studentService.add(HARRY));
    }

    @Test
    void get_success() {
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));
        assertEquals(HARRY, studentService.get(HARRY.getId()));
    }

    @Test
    void get_StudentNotFoundException() {
        when(repository.findById(DRACO.getId())).thenThrow(StudentNotFoundException.class);
        assertThrows(StudentNotFoundException.class, () -> studentService.get(DRACO.getId()));
    }

    @Test
    void edit_success() {
        when(repository.save(EDIT_STUDENT)).thenReturn(EDIT_STUDENT);
        assertEquals(EDIT_STUDENT, studentService.edit(HARRY.getId(), DRACO));
    }

    @Test
    void edit_InvalideInputException() {
        when(repository.save(INVALIDE_STUDENT)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> studentService.add(INVALIDE_STUDENT));
    }

    @Test
    void remove_success() {
        studentService.remove(HARRY.getId());
        verify(repository, only()).deleteById(HARRY.getId());
    }

    @Test
    void getAll_success() {
        when(repository.findAll()).thenReturn(getStudents());
        assertEquals(getStudents(), studentService.getAll());
    }

    @Test
    void getByAge_success() {
        when(repository.findByAge(AGE)).thenReturn(getStudents());
        assertEquals(getStudents(), studentService.getByAge(AGE));
    }
}