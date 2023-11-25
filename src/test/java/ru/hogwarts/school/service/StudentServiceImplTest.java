package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.*;
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
        when(repository.findAll()).thenReturn(getStudents());
        when(repository.save(HARRY)).thenReturn(HARRY);

        assertEquals(HARRY, studentService.add(HARRY));

        verify(checkService, times(1)).validateCheck(HARRY);
        verify(checkService, times(1)).isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void add_InvalideInputException() {
        when(repository.findAll()).thenReturn(getStudents());
        when(repository.save(HARRY)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.add(HARRY));

        verify(checkService, times(1)).validateCheck(HARRY);
        verify(checkService, times(1)).isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void add_StudentAlreadyAddedException() {
        when(repository.findAll()).thenReturn(getStudents());
        when(repository.save(HARRY)).thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class, () -> studentService.add(HARRY));

        verify(checkService, times(1)).validateCheck(HARRY);
        verify(checkService, times(1)).isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void get_success() {
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.get(HARRY.getId()));

        verify(checkService, times(1)).validateCheck(HARRY.getId());
        verify(repository, times(1)).findById(HARRY.getId());
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(HARRY.getId())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.get(HARRY.getId()));

        verify(checkService, times(1)).validateCheck(HARRY.getId());
        verify(repository, times(1)).findById(HARRY.getId());
    }

    @Test
    void get_StudentNotFoundException() {
        when(repository.findById(HARRY.getId())).thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class, () -> studentService.get(HARRY.getId()));

        verify(checkService, times(1)).validateCheck(HARRY.getId());
        verify(repository, times(1)).findById(HARRY.getId());

    }

    @Test
    void edit_success() {
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));
        when(repository.save(EDIT_STUDENT)).thenReturn(EDIT_STUDENT);

        assertEquals(EDIT_STUDENT, studentService.edit(HARRY.getId(), DRACO));

        verify(checkService, times(1)).validateCheck(HARRY.getId());
        verify(checkService, times(1)).validateCheck(DRACO);
        verify(repository, times(1)).findById(HARRY.getId());
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void edit_InvalideInputException() {
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));
        when(repository.save(EDIT_STUDENT)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.edit(HARRY.getId(), DRACO));

        verify(checkService, times(1)).validateCheck(HARRY.getId());
        verify(checkService, times(1)).validateCheck(DRACO);
        verify(repository, times(1)).findById(HARRY.getId());
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void remove_success() {
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.remove(HARRY.getId()));

        verify(checkService, times(1)).validateCheck(HARRY.getId());
        verify(repository, times(1)).findById(HARRY.getId());
        verify(repository, times(1)).delete(HARRY);
    }

    @Test
    void getAll_success() {
        when(repository.findAll()).thenReturn(getStudents());

        assertEquals(getStudents(), studentService.getAll());

        verify(repository, times(1)).findAll();

    }

    @Test
    void getByAge_WithOnlyFirstParameter_success() {
        when(repository.findByAge(HARRY.getAge())).thenReturn(List.of(HARRY));

        assertEquals(List.of(HARRY), studentService.getByAge(HARRY.getAge(), null));

        verify(checkService, times(1)).validateCheck(HARRY.getAge());
        verify(repository, times(1)).findByAge(HARRY.getAge());
    }

    @Test
    void getByAge_WithOnlyFirstParameter_InvalideInputException() {
        when(repository.findByAge(HARRY.getAge())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.getByAge(HARRY.getAge(), null));

        verify(checkService, times(1)).validateCheck(HARRY.getAge());
        verify(repository, times(1)).findByAge(HARRY.getAge());
    }

    @Test
    void getByAge_WithOnlySecondParameter_success() {
        when(repository.findByAge(HARRY.getAge())).thenReturn(List.of(HARRY));

        assertEquals(List.of(HARRY), studentService.getByAge(null, HARRY.getAge()));

        verify(checkService, times(1)).validateCheck(HARRY.getAge());
        verify(repository, times(1)).findByAge(HARRY.getAge());
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() {
        when(repository.findByAge(HARRY.getAge())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.getByAge(null, HARRY.getAge()));

        verify(checkService, times(1)).validateCheck(HARRY.getAge());
        verify(repository, times(1)).findByAge(HARRY.getAge());
    }

    @Test
    void getByAge_WithAllParameters_success() {
        when(repository.findByAgeBetween(HARRY.getAge(), HERMIONE.getAge())).thenReturn(List.of(HARRY, RON, HERMIONE));

        assertEquals(List.of(HARRY, RON, HERMIONE), studentService.getByAge(HARRY.getAge(), HERMIONE.getAge()));

        verify(checkService, times(1)).validateCheck(HARRY.getAge(), HERMIONE.getAge());
        verify(repository, times(1)).findByAgeBetween(HARRY.getAge(), HERMIONE.getAge());
    }

    @Test
    void getByAge_WithAllParameters_InvalideInputException() {
        when(repository.findByAgeBetween(HARRY.getAge(), HERMIONE.getAge())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.getByAge(HARRY.getAge(), HERMIONE.getAge()));

        verify(checkService, times(1)).validateCheck(HARRY.getAge(), HERMIONE.getAge());
        verify(repository, times(1)).findByAgeBetween(HARRY.getAge(), HERMIONE.getAge());
    }

    @Test
    void getByAge_WithoutParameters_success() {
        when(repository.findAll()).thenReturn(getStudents());

        assertEquals(getStudents(), studentService.getByAge(null, null));

        verify(repository, times(1)).findAll();
    }

    @Test
    void getFacultyById_success() {
        Student HARRY = new Student(1L, "Гарри Поттер", 12, GRIFFINDOR);
        when(repository.findById(HARRY.getId())).thenReturn(Optional.of(HARRY));

        assertEquals(GRIFFINDOR, studentService.getFacultyById(HARRY.getId()));

        verify(checkService, times(1)).validateCheck(HARRY.getId());
        verify(repository, times(1)).findById(HARRY.getId());
    }
}