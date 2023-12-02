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
        HARRY.getFaculty().setId(GRIFFINDOR_ID);

        when(repository.findAll()).thenReturn(getStudents());
        when(repository.save(HARRY)).thenReturn(HARRY);

        assertEquals(HARRY, studentService.add(HARRY.getName(), HARRY.getAge(), HARRY.getFaculty().getId()));

        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).validateCheck(HARRY);
        verify(checkService, times(1)).isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void add_InvalideInputException() {
        HARRY.getFaculty().setId(GRIFFINDOR_ID);

        when(repository.findAll()).thenReturn(getStudents());
        when(repository.save(HARRY)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.add(HARRY.getName(), HARRY.getAge(), HARRY.getFaculty().getId()));

        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).validateCheck(HARRY);
        verify(checkService, times(1)).isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void add_StudentAlreadyAddedException() {
        HARRY.getFaculty().setId(GRIFFINDOR_ID);

        when(repository.findAll()).thenReturn(getStudents());
        when(repository.save(HARRY)).thenThrow(FacultyAlreadyAddedException.class);

        assertThrows(FacultyAlreadyAddedException.class, () -> studentService.add(HARRY.getName(), HARRY.getAge(), HARRY.getFaculty().getId()));

        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).validateCheck(HARRY);
        verify(checkService, times(1)).isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1)).save(HARRY);
    }

    @Test
    void get_success() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.get(HARRY_ID));

        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(HARRY_ID)).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.get(HARRY_ID));

        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
    }

    @Test
    void get_StudentNotFoundException() {
        when(repository.findById(HARRY_ID)).thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class, () -> studentService.get(HARRY_ID));

        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);

    }

    @Test
    void edit_WithOnlyName_success() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), HARRY.getAge(), HARRY.getFaculty())))
                .thenReturn(new Student(DRACO.getName(), HARRY.getAge(), HARRY.getFaculty()));

        assertEquals(new Student(DRACO.getName(), HARRY.getAge(), HARRY.getFaculty()),
                studentService.edit(HARRY_ID, DRACO.getName(), null, null));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(DRACO.getName(), HARRY.getAge(), HARRY.getFaculty()));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), HARRY.getAge(), HARRY.getFaculty())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO.getName(), null, null));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(DRACO.getName(), HARRY.getAge(), HARRY.getFaculty()));
    }

    @Test
    void edit_WithOnlyNameAndAge_success() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), DRACO.getAge(), HARRY.getFaculty())))
                .thenReturn(new Student(DRACO.getName(), DRACO.getAge(), HARRY.getFaculty()));

        assertEquals(new Student(DRACO.getName(), DRACO.getAge(), HARRY.getFaculty()),
                studentService.edit(HARRY_ID, DRACO.getName(), DRACO.getAge(), null));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(DRACO.getName(), DRACO.getAge(), HARRY.getFaculty()));
    }

    @Test
    void edit_WithOnlyNameAndAge_InvalideInputException() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), DRACO.getAge(), HARRY.getFaculty())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO.getName(), DRACO.getAge(), null));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(DRACO.getName(), DRACO.getAge(), HARRY.getFaculty()));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_success() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), HARRY.getAge(), DRACO.getFaculty())))
                .thenReturn(new Student(DRACO.getName(), HARRY.getAge(), DRACO.getFaculty()));

        assertEquals(new Student(DRACO.getName(), HARRY.getAge(), DRACO.getFaculty()),
                studentService.edit(HARRY_ID, DRACO.getName(), HARRY.getAge(), DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(DRACO.getName(), HARRY.getAge(), DRACO.getFaculty()));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_InvalideInputException() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), HARRY.getAge(), DRACO.getFaculty())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO.getName(), HARRY.getAge(), DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(DRACO.getName(), HARRY.getAge(), DRACO.getFaculty()));
    }

    @Test
    void edit_WithOnlyAge_success() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(HARRY.getName(), DRACO.getAge(), HARRY.getFaculty())))
                .thenReturn(new Student(HARRY.getName(), DRACO.getAge(), HARRY.getFaculty()));

        assertEquals(new Student(HARRY.getName(), DRACO.getAge(), HARRY.getFaculty()),
                studentService.edit(HARRY_ID, null, DRACO.getAge(), null));

        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(HARRY.getName(), DRACO.getAge(), HARRY.getFaculty()));
    }

    @Test
    void edit_WithOnlyAge_InvalideInputException() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(HARRY.getName(), DRACO.getAge(), HARRY.getFaculty())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, null, DRACO.getAge(), null));

        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(HARRY.getName(), DRACO.getAge(), HARRY.getFaculty()));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_success() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(HARRY.getName(), DRACO.getAge(), DRACO.getFaculty())))
                .thenReturn(new Student(HARRY.getName(), DRACO.getAge(), DRACO.getFaculty()));

        assertEquals(new Student(HARRY.getName(), DRACO.getAge(), DRACO.getFaculty()),
                studentService.edit(HARRY_ID, null, DRACO.getAge(), DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(HARRY.getName(), DRACO.getAge(), DRACO.getFaculty()));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_InvalideInputException() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(HARRY.getName(), DRACO.getAge(), DRACO.getFaculty())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, null, DRACO.getAge(), DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(HARRY.getName(), DRACO.getAge(), HARRY.getFaculty()));
    }

    @Test
    void edit_WithOnlyFacultyId_success() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(HARRY.getName(), HARRY.getAge(), DRACO.getFaculty())))
                .thenReturn(new Student(HARRY.getName(), HARRY.getAge(), DRACO.getFaculty()));

        assertEquals(new Student(HARRY.getName(), HARRY.getAge(), DRACO.getFaculty()),
                studentService.edit(HARRY_ID, null, null, DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(HARRY.getName(), HARRY.getAge(), DRACO.getFaculty()));
    }

    @Test
    void edit_WithOnlyFacultyId_InvalideInputException() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(HARRY.getName(), HARRY.getAge(), DRACO.getFaculty())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, null, null, DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(HARRY.getName(), HARRY.getAge(), DRACO.getFaculty()));
    }

    @Test
    void edit_WithAllParameters_success() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), DRACO.getAge(), DRACO.getFaculty())))
                .thenReturn(new Student(DRACO.getName(), DRACO.getAge(), DRACO.getFaculty()));

        assertEquals(new Student(DRACO.getName(), DRACO.getAge(), DRACO.getFaculty()),
                studentService.edit(HARRY_ID, DRACO.getName(), DRACO.getAge(), DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(HARRY.getName(), HARRY.getAge(), DRACO.getFaculty()));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        DRACO.getFaculty().setId(SLYTHERIN_ID);

        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));
        when(repository.save(new Student(DRACO.getName(), DRACO.getAge(), DRACO.getFaculty())))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO.getName(), DRACO.getAge(), DRACO.getFaculty().getId()));

        verify(checkService, times(1)).validateCheck(DRACO.getName());
        verify(checkService, times(1)).validateCheck(DRACO.getAge());
        verify(checkService, times(1)).validateCheck(DRACO.getFaculty().getId());
        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
        verify(repository, times(1)).save(new Student(DRACO.getName(), DRACO.getAge(), DRACO.getFaculty()));
    }

    @Test
    void edit_WithoutParameters_success() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.edit(HARRY_ID, null, null, null));

        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
    }

    @Test
    void remove_success() {
        when(repository.findById(HARRY_ID)).thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.remove(HARRY_ID));

        verify(checkService, times(1)).validateCheck(HARRY_ID);
        verify(repository, times(1)).findById(HARRY_ID);
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
        when(repository.findByAge(HERMIONE.getAge())).thenReturn(List.of(HERMIONE));

        assertEquals(List.of(HERMIONE), studentService.getByAge(null, HERMIONE.getAge()));

        verify(checkService, times(1)).validateCheck(HERMIONE.getAge());
        verify(repository, times(1)).findByAge(HERMIONE.getAge());
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() {
        when(repository.findByAge(HERMIONE.getAge())).thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class, () -> studentService.getByAge(null, HERMIONE.getAge()));

        verify(checkService, times(1)).validateCheck(HERMIONE.getAge());
        verify(repository, times(1)).findByAge(HERMIONE.getAge());
    }

    @Test
    void getByAge_WithAllParameters_success() {
        when(repository.findByAgeBetween(HARRY.getAge(), HERMIONE.getAge())).thenReturn(List.of(HARRY, HERMIONE));

        assertEquals(List.of(HARRY, HERMIONE), studentService.getByAge(HARRY.getAge(), HERMIONE.getAge()));

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
        Student Harry = new Student(HARRY.getName(), HARRY.getAge(), GRIFFINDOR);
        when(repository.findById(Harry.getId())).thenReturn(Optional.of(Harry));

        assertEquals(GRIFFINDOR, studentService.getFacultyById(Harry.getId()));

        verify(checkService, times(1)).validateCheck(Harry.getId());
        verify(repository, times(1)).findById(Harry.getId());
    }
}