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
import ru.hogwarts.school.model.Faculty;
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
    FacultyService facultyService;
    @Mock
    private CheckService checkService;
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImpl(repository, facultyService, checkService);

        GRIFFINDOR.setId(GRIFFINDOR_ID);
    }

    @Test
    void add_success() {
        when(facultyService.get(any(Long.class)))
                .thenReturn(GRIFFINDOR);
        when(repository.findAll())
                .thenReturn(getStudents());
        when(repository.save(any(Student.class)))
                .thenReturn(HARRY);

        HARRY.getFaculty().setId(GRIFFINDOR_ID);

        assertEquals(HARRY, studentService.add(HARRY_NAME, HARRY_AGE, GRIFFINDOR_ID));

        verify(repository, times(1))
                .findAll();
        verify(checkService, times(1))
                .validateCheck(HARRY);
        verify(checkService, times(1))
                .isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1))
                .save(HARRY);
        verify(facultyService, times(1))
                .get(GRIFFINDOR_ID);
    }

    @Test
    void add_InvalideInputException() {
        when(facultyService.get(any(Long.class)))
                .thenReturn(GRIFFINDOR);
        when(repository.findAll())
                .thenReturn(getStudents());
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        HARRY.getFaculty().setId(GRIFFINDOR_ID);

        assertThrows(InvalideInputException.class,
                () -> studentService.add(HARRY_NAME, HARRY_AGE, GRIFFINDOR_ID));

        verify(repository, times(1))
                .findAll();
        verify(checkService, times(1))
                .validateCheck(HARRY);
        verify(checkService, times(1))
                .isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1))
                .save(HARRY);
        verify(facultyService, times(1))
                .get(GRIFFINDOR_ID);
    }

    @Test
    void add_StudentAlreadyAddedException() {
        when(facultyService.get(any(Long.class)))
                .thenReturn(GRIFFINDOR);
        when(repository.findAll())
                .thenReturn(getStudents());
        when(repository.save(any(Student.class)))
                .thenThrow(FacultyAlreadyAddedException.class);

        HARRY.getFaculty().setId(GRIFFINDOR_ID);

        assertThrows(FacultyAlreadyAddedException.class,
                () -> studentService.add(HARRY_NAME, HARRY_AGE, GRIFFINDOR_ID));

        verify(repository, times(1))
                .findAll();
        verify(checkService, times(1))
                .validateCheck(HARRY);
        verify(checkService, times(1))
                .isStudentAlreadyAdded(getStudents(), HARRY);
        verify(repository, times(1))
                .save(HARRY);
        verify(facultyService, times(1))
                .get(GRIFFINDOR_ID);
    }

    @Test
    void get_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.get(HARRY_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.get(HARRY_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
    }

    @Test
    void get_StudentNotFoundException() {
        when(repository.findById(any(Long.class)))
                .thenThrow(FacultyNotFoundException.class);

        assertThrows(FacultyNotFoundException.class,
                () -> studentService.get(HARRY_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
    }

    @Test
    void edit_WithOnlyName_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, HARRY_AGE, new Faculty()));

        assertEquals(new Student(DRACO_NAME, HARRY_AGE, new Faculty()),
                studentService.edit(HARRY_ID, DRACO_NAME, null, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, HARRY_AGE, new Faculty()));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))

                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO_NAME, null, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, HARRY_AGE, new Faculty()));
    }

    @Test
    void edit_WithOnlyNameAndAge_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))

                .thenReturn(new Student(DRACO_NAME, DRACO_AGE, new Faculty()));

        assertEquals(new Student(DRACO_NAME, DRACO_AGE, new Faculty()),
                studentService.edit(HARRY_ID, DRACO_NAME, DRACO_AGE, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, DRACO_AGE, new Faculty()));
    }

    @Test
    void edit_WithOnlyNameAndAge_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO_NAME, DRACO_AGE, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, DRACO_AGE, new Faculty()));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_success() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, HARRY_AGE, DRACO.getFaculty()));

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertEquals(new Student(DRACO_NAME, HARRY_AGE, DRACO.getFaculty()),
                studentService.edit(HARRY_ID, DRACO_NAME, null, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, HARRY_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_InvalideInputException() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO_NAME, null, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, HARRY_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithOnlyAge_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, DRACO_AGE, new Faculty()));

        assertEquals(new Student(HARRY_NAME, DRACO_AGE, new Faculty()),
                studentService.edit(HARRY_ID, null, DRACO_AGE, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(HARRY_NAME, DRACO_AGE, new Faculty()));
    }

    @Test
    void edit_WithOnlyAge_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, null, DRACO_AGE, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(HARRY_NAME, DRACO_AGE, new Faculty()));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_success() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, DRACO_AGE, DRACO.getFaculty()));

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertEquals(new Student(HARRY_NAME, DRACO_AGE, DRACO.getFaculty()),
                studentService.edit(HARRY_ID, null, DRACO_AGE, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(HARRY_NAME, DRACO_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_InvalideInputException() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, null, DRACO_AGE, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(HARRY_NAME, DRACO_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithOnlyFacultyId_success() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, HARRY_AGE, DRACO.getFaculty()));

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertEquals(new Student(HARRY_NAME, HARRY_AGE, DRACO.getFaculty()),
                studentService.edit(HARRY_ID, null, null, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(HARRY_NAME, HARRY_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithOnlyFacultyId_InvalideInputException() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, null, null, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(HARRY_NAME, HARRY_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithAllParameters_success() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, DRACO_AGE, DRACO.getFaculty()));

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertEquals(new Student(DRACO_NAME, DRACO_AGE, DRACO.getFaculty()),
                studentService.edit(HARRY_ID, DRACO_NAME, DRACO_AGE, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, DRACO_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        SLYTHERIN.setId(SLYTHERIN_ID);
        DRACO.setFaculty(new Faculty());

        when(facultyService.get(any(Long.class)))
                .thenReturn(SLYTHERIN);
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        DRACO.getFaculty().setId(SLYTHERIN_ID);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(HARRY_ID, DRACO_NAME, DRACO_AGE, SLYTHERIN_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .save(new Student(DRACO_NAME, DRACO_AGE, DRACO.getFaculty()));
        verify(facultyService, times(1))
                .get(SLYTHERIN_ID);
    }

    @Test
    void edit_WithoutParameters_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.edit(HARRY_ID, null, null, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
    }

    @Test
    void remove_success() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        assertEquals(HARRY, studentService.remove(HARRY_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
        verify(repository, times(1))
                .delete(HARRY);
    }

    @Test
    void getAll_success() {
        when(repository.findAll())
                .thenReturn(getStudents());

        assertEquals(getStudents(), studentService.getAll());

        verify(repository, times(1))
                .findAll();

    }

    @Test
    void getByAge_WithOnlyFirstParameter_success() {
        when(repository.findByAge(any(Integer.class)))
                .thenReturn(List.of(HARRY));

        assertEquals(List.of(HARRY), studentService.getByAge(HARRY_AGE, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_AGE);
        verify(repository, times(1))
                .findByAge(HARRY_AGE);
    }

    @Test
    void getByAge_WithOnlyFirstParameter_InvalideInputException() {
        when(repository.findByAge(any(Integer.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(HARRY_AGE, null));

        verify(checkService, times(1))
                .validateCheck(HARRY_AGE);
        verify(repository, times(1))
                .findByAge(HARRY_AGE);
    }

    @Test
    void getByAge_WithOnlySecondParameter_success() {
        when(repository.findByAge(any(Integer.class)))
                .thenReturn(List.of(HERMIONE));

        assertEquals(List.of(HERMIONE),
                studentService.getByAge(null, HERMIONE_AGE));

        verify(checkService, times(1))
                .validateCheck(HERMIONE_AGE);
        verify(repository, times(1))
                .findByAge(HERMIONE_AGE);
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() {
        when(repository.findByAge(any(Integer.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(null, HERMIONE_AGE));

        verify(checkService, times(1))
                .validateCheck(HERMIONE_AGE);
        verify(repository, times(1))
                .findByAge(HERMIONE_AGE);
    }

    @Test
    void getByAge_WithAllParameters_success() {
        when(repository.findByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(HARRY, HERMIONE));

        assertEquals(List.of(HARRY, HERMIONE),
                studentService.getByAge(HARRY_AGE, HERMIONE_AGE));

        verify(checkService, times(1))
                .validateCheck(HARRY_AGE, HERMIONE_AGE);
        verify(repository, times(1))
                .findByAgeBetween(HARRY_AGE, HERMIONE_AGE);
    }

    @Test
    void getByAge_WithAllParameters_InvalideInputException() {
        when(repository.findByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(HARRY_AGE, HERMIONE_AGE));

        verify(checkService, times(1))
                .validateCheck(HARRY_AGE, HERMIONE_AGE);
        verify(repository, times(1))
                .findByAgeBetween(HARRY_AGE, HERMIONE_AGE);
    }

    @Test
    void getByAge_WithoutParameters_success() {
        when(repository.findAll())
                .thenReturn(getStudents());

        assertEquals(getStudents(), studentService.getByAge(null, null));

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void getFacultyById_success() {
        GRIFFINDOR.setId(GRIFFINDOR_ID);
        HARRY.setFaculty(GRIFFINDOR);
        HARRY.setId(HARRY_ID);

        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        assertEquals(GRIFFINDOR, studentService.getFacultyById(HARRY_ID));

        verify(checkService, times(1))
                .validateCheck(HARRY_ID);
        verify(repository, times(1))
                .findById(HARRY_ID);
    }
}