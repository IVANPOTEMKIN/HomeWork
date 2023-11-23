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
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.util.List;
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
    }

    @Test
    void add_success() {
        when(repository.save(DRACO)).thenReturn(DRACO);
        assertEquals(DRACO, studentService.add(DRACO));
    }

    @Test
    void add_InvalideInputException() {
        when(repository.save(INVALIDE_STUDENT)).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> studentService.add(INVALIDE_STUDENT));
    }

    @Test
    void add_StudentAlreadyAddedException() {
        when(repository.save(HARRY)).thenThrow(StudentAlreadyAddedException.class);
        assertThrows(StudentAlreadyAddedException.class, () -> studentService.add(HARRY));
    }

    @Test
    void get_success() {
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));
        assertEquals(HARRY, studentService.get(HARRY.getId()));
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(INVALIDE_STUDENT.getId())).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> studentService.get(INVALIDE_STUDENT.getId()));
    }

    @Test
    void get_StudentNotFoundException() {
        when(repository.findById(DRACO.getId())).thenThrow(StudentNotFoundException.class);
        assertThrows(StudentNotFoundException.class, () -> studentService.get(DRACO.getId()));
    }

    @Test
    void edit_success() {
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));
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
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));
        studentService.remove(HARRY.getId());
        verify(repository, times(1)).deleteById(HARRY.getId());
    }

    @Test
    void getAll_success() {
        when(repository.findAll()).thenReturn(getStudents());
        assertEquals(getStudents(), studentService.getAll());
    }

    @Test
    void getByAge_WithOnlyFirstParameter_success() {
        when(repository.findByAge(HARRY.getAge())).thenReturn(List.of(HARRY));
        assertEquals(List.of(HARRY), studentService.getByAge(HARRY.getAge(), null));
    }

    @Test
    void getByAge_WithOnlyFirstParameter_InvalideInputException() {
        when(repository.findByAge(HARRY.getAge())).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> studentService.getByAge(HARRY.getAge(), null));
    }

    @Test
    void getByAge_WithOnlySecondParameter_success() {
        when(repository.findByAge(HERMIONE.getAge())).thenReturn(List.of(HERMIONE));
        assertEquals(List.of(HERMIONE), studentService.getByAge(null, HERMIONE.getAge()));
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() {
        when(repository.findByAge(HERMIONE.getAge())).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> studentService.getByAge(null, HERMIONE.getAge()));
    }

    @Test
    void getByAge_WithAllParameters_success() {
        when(repository.findByAgeBetween(HARRY.getAge(), HERMIONE.getAge())).thenReturn(getStudents());
        assertEquals(getStudents(), studentService.getByAge(HARRY.getAge(), HERMIONE.getAge()));
    }

    @Test
    void getByAge_WithAllParameters_InvalideInputException() {
        when(repository.findByAgeBetween(HARRY.getAge(), HERMIONE.getAge())).thenThrow(InvalideInputException.class);
        assertThrows(InvalideInputException.class, () -> studentService.getByAge(HARRY.getAge(), HERMIONE.getAge()));
    }

    @Test
    void getByAge_WithoutParameters_success() {
        when(repository.findAll()).thenReturn(getStudents());
        assertEquals(getStudents(), studentService.getByAge(null, null));
    }

    @Test
    void getFacultyById_success() {
        Student HARRY = new Student(1L, "Гарри Поттер", 12, GRIFFINDOR);
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));
        assertEquals(GRIFFINDOR, studentService.getFacultyById(HARRY.getId()));
    }
}