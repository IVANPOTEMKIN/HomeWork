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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;
import static ru.hogwarts.school.utils.Examples.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repository;
    @Mock
    private FacultyService facultyService;
    @Mock
    private CheckService checkService;
    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentServiceImpl(repository, facultyService, checkService);
    }

    private void getGriffindor() {
        when(facultyService.get(GRIFFINDOR_ID))
                .thenReturn(GRIFFINDOR);

        GRIFFINDOR.setId(GRIFFINDOR_ID);
    }

    @Test
    void add_success() {
        getGriffindor();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenReturn(false);

        HARRY.setFaculty(GRIFFINDOR);

        when(repository.save(any(Student.class)))
                .thenReturn(HARRY);

        assertEquals(HARRY,
                studentService.add(
                        HARRY_NAME,
                        HARRY_AGE,
                        GRIFFINDOR.getId()));

        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void add_InvalideInputException() {
        getGriffindor();

        when(checkService.validateCheck(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.add(
                        INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT,
                        GRIFFINDOR.getId()));

        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void add_StudentAlreadyAddedException() {
        getGriffindor();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.add(
                        HARRY_NAME,
                        HARRY_AGE,
                        GRIFFINDOR.getId()));

        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void get_success() {
        when(checkService.validateCheck(anyLong()))
                .thenReturn(false);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(HARRY));

        HARRY.setId(HARRY_ID);

        assertEquals(HARRY,
                studentService.get(HARRY.getId()));

        verify(checkService, times(1)).validateCheck(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void get_InvalideInputException() {
        when(checkService.validateCheck(anyLong()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.get(INVALIDE_ID));

        verify(checkService, times(1)).validateCheck(anyLong());
        verify(repository, times(0)).findById(anyLong());
    }

    @Test
    void get_StudentNotFoundException() {
        when(checkService.validateCheck(anyLong()))
                .thenReturn(false);
        when(repository.findById(anyLong()))
                .thenThrow(StudentNotFoundException.class);

        assertThrows(StudentNotFoundException.class,
                () -> studentService.get(HARRY_ID));

        verify(checkService, times(1)).validateCheck(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void edit_WithOnlyName_success() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenReturn(false);

        Student expected = new Student(DRACO_NAME, HARRY_AGE, new Faculty());

        when(repository.save(any(Student.class)))
                .thenReturn(expected);

        assertEquals(expected,
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        null,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        null,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyName_StudentAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        null,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyNameAndAge_success() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenReturn(false);

        Student expected = new Student(DRACO_NAME, DRACO_AGE, new Faculty());

        when(repository.save(any(Student.class)))
                .thenReturn(expected);

        assertEquals(expected,
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        DRACO_AGE,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyNameAndAge_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyNameAndAge_StudentAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        DRACO_AGE,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_success() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getGriffindor();
        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenReturn(false);

        Student expected = new Student(DRACO_NAME, HARRY_AGE, GRIFFINDOR);

        when(repository.save(any(Student.class)))
                .thenReturn(expected);

        assertEquals(expected,
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        null,
                        GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        null,
                        INVALIDE_ID));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(0)).get(GRIFFINDOR_ID);
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_StudentAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getGriffindor();
        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        null,
                        GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyAge_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        null,
                        INVALIDE_AGE_STUDENT,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyAge_StudentAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        null,
                        DRACO_AGE,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_success() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getGriffindor();
        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenReturn(false);

        Student expected = new Student(HARRY_NAME, DRACO_AGE, GRIFFINDOR);

        when(repository.save(any(Student.class)))
                .thenReturn(expected);

        assertEquals(expected,
                studentService.edit(
                        HARRY.getId(),
                        null,
                        DRACO_AGE,
                        GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyAge_success() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenReturn(false);

        Student expected = new Student(HARRY_NAME, DRACO_AGE, new Faculty());

        when(repository.save(any(Student.class)))
                .thenReturn(expected);

        assertEquals(expected,
                studentService.edit(
                        HARRY.getId(),
                        null,
                        DRACO_AGE,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        null,
                        INVALIDE_AGE_STUDENT,
                        INVALIDE_ID));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(0)).get(GRIFFINDOR_ID);
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_StudentAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getGriffindor();
        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        null,
                        DRACO_AGE,
                        GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithOnlyFacultyId_success() {
        get_success();
        getGriffindor();

        Student expected = new Student(HARRY_NAME, HARRY_AGE, GRIFFINDOR);

        when(repository.save(any(Student.class)))
                .thenReturn(expected);

        assertEquals(expected,
                studentService.edit(
                        HARRY.getId(),
                        null,
                        null,
                        GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void edit_WithAllParameters_success() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getGriffindor();
        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenReturn(false);

        Student expected = new Student(DRACO_NAME, DRACO_AGE, GRIFFINDOR);

        when(repository.save(any(Student.class)))
                .thenReturn(expected);

        assertEquals(expected,
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        DRACO_AGE,
                        GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT,
                        INVALIDE_ID));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(0)).get(GRIFFINDOR_ID);
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithAllParameters_StudentAlreadyAddedException() {
        get_success();

        when(checkService.validateCheck(any(Student.class)))
                .thenReturn(false);

        getGriffindor();
        getAll_success();

        when(checkService.isStudentAlreadyAdded(anyCollection(), any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        DRACO_AGE,
                        GRIFFINDOR.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).validateCheck(any(Student.class));
        verify(facultyService, times(1)).get(GRIFFINDOR_ID);
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).isStudentAlreadyAdded(anyCollection(), any(Student.class));
        verify(repository, times(0)).save(any(Student.class));
    }

    @Test
    void edit_WithoutParameters_success() {
        get_success();

        assertEquals(HARRY,
                studentService.edit(
                        HARRY.getId(),
                        null,
                        null,
                        null));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
    }

    @Test
    void remove_success() {
        get_success();

        assertEquals(HARRY,
                studentService.remove(HARRY.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(1)).delete(any(Student.class));
    }

    @Test
    void getAll_success() {
        when(repository.findAll())
                .thenReturn(getStudents());

        assertEquals(getStudents(),
                studentService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    void getByAge_WithOnlyFirstParameter_success() {
        when(checkService.validateCheck(anyInt()))
                .thenReturn(false);
        when(repository.findByAge(anyInt()))
                .thenReturn(List.of(HARRY));

        assertEquals(List.of(HARRY),
                studentService.getByAge(
                        HARRY_AGE,
                        null));

        verify(checkService, times(1)).validateCheck(anyInt());
        verify(repository, times(1)).findByAge(anyInt());
    }

    @Test
    void getByAge_WithOnlyFirstParameter_InvalideInputException() {
        when(checkService.validateCheck(anyInt()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(
                        INVALIDE_AGE_STUDENT,
                        null));

        verify(checkService, times(1)).validateCheck(anyInt());
        verify(repository, times(0)).findByAge(anyInt());
    }

    @Test
    void getByAge_WithOnlySecondParameter_success() {
        when(checkService.validateCheck(anyInt()))
                .thenReturn(false);
        when(repository.findByAge(anyInt()))
                .thenReturn(List.of(HERMIONE));

        assertEquals(List.of(HERMIONE),
                studentService.getByAge(
                        HERMIONE_AGE,
                        null));

        verify(checkService, times(1)).validateCheck(anyInt());
        verify(repository, times(1)).findByAge(anyInt());
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() {
        when(checkService.validateCheck(anyInt()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(
                        null,
                        INVALIDE_AGE_STUDENT));

        verify(checkService, times(1)).validateCheck(anyInt());
        verify(repository, times(0)).findByAge(anyInt());
    }

    @Test
    void getByAge_WithAllParameters_success() {
        when(checkService.validateCheck(anyInt(), anyInt()))
                .thenReturn(false);
        when(repository.findByAgeBetween(anyInt(), anyInt()))
                .thenReturn(getStudents());

        assertEquals(getStudents(),
                studentService.getByAge(
                        HARRY_AGE,
                        HERMIONE_AGE));

        verify(checkService, times(1)).validateCheck(anyInt(), anyInt());
        verify(repository, times(1)).findByAgeBetween(anyInt(), anyInt());
    }

    @Test
    void getByAge_WithAllParameters_InvalideInputException() {
        when(checkService.validateCheck(anyInt(), anyInt()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(
                        HERMIONE_AGE,
                        HARRY_AGE));

        verify(checkService, times(1)).validateCheck(anyInt(), anyInt());
        verify(repository, times(0)).findByAge(anyInt());
    }

    @Test
    void getByAge_WithoutParameters_success() {
        getAll_success();

        assertEquals(getStudents(),
                studentService.getByAge(
                        null,
                        null));

        verify(repository, times(2)).findAll();
    }

    @Test
    void getByName_success() {
        when(checkService.validateCheck(anyString()))
                .thenReturn(false);
        when(repository.findByNameContainsIgnoreCase(anyString()))
                .thenReturn(List.of(HARRY));

        assertEquals(List.of(HARRY),
                studentService.getByName(HARRY_NAME));

        verify(checkService, times(1)).validateCheck(anyString());
        verify(repository, times(1)).findByNameContainsIgnoreCase(anyString());
    }

    @Test
    void getByName_InvalideInputException() {
        when(checkService.validateCheck(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByName(INVALIDE_NAME_STUDENT));

        verify(checkService, times(1)).validateCheck(anyString());
        verify(repository, times(0)).findByNameContainsIgnoreCase(anyString());
    }

    @Test
    void getByName_WithoutParameters_success() {
        getAll_success();

        assertEquals(getStudents(),
                studentService.getByName(null));

        verify(repository, times(2)).findAll();
    }

    @Test
    void getFacultyById_success() {
        get_success();

        GRIFFINDOR.setId(GRIFFINDOR_ID);
        HARRY.setFaculty(GRIFFINDOR);

        assertEquals(GRIFFINDOR,
                studentService.getFacultyById(HARRY.getId()));

        verify(checkService, times(2)).validateCheck(anyLong());
        verify(repository, times(2)).findById(anyLong());
    }

    @Test
    void getAmountAllFaculties_success() {
        when(repository.getAmountAllStudents())
                .thenReturn(AMOUNT_STUDENTS);

        assertEquals(AMOUNT_STUDENTS,
                studentService.getAmountAllStudents());

        verify(repository, times(1)).getAmountAllStudents();
    }

    @Test
    void getAvgAgeStudents_success() {
        DecimalFormat numberFormat = new DecimalFormat("#.#");

        when(repository.getAvgAgeStudents())
                .thenReturn(AVG_AGE_STUDENTS);

        assertEquals(numberFormat.format(AVG_AGE_STUDENTS),
                studentService.getAvgAgeStudents());

        verify(repository, times(1)).getAvgAgeStudents();
    }

    @Test
    void getLastFiveStudents_success() {
        when(repository.getLastFiveStudents())
                .thenReturn(getStudents());

        assertEquals(getStudents(),
                studentService.getLastFiveStudents());

        verify(repository, times(1)).getLastFiveStudents();
    }

    @Test
    void getBySortedName_WithPrefix_success() {
        getAll_success();

        when(checkService.validateCheck(anyString()))
                .thenReturn(false);

        Collection<String> expected = List.of(HARRY_NAME.toUpperCase(), HERMIONE_NAME.toUpperCase());

        assertEquals(expected,
                studentService.getBySortedName(PREFIX));

        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).validateCheck(anyString());
    }

    @Test
    void getBySortedName_WithPrefix_InvalideInputException() {
        getAll_success();

        when(checkService.validateCheck(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getBySortedName(INVALIDE_PREFIX));

        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).validateCheck(anyString());
    }

    @Test
    void getBySortedName_WithoutPrefix_success() {
        getAll_success();

        Collection<String> expected = List.of(HARRY_NAME.toUpperCase(), HERMIONE_NAME.toUpperCase());

        assertEquals(expected,
                studentService.getBySortedName(null));

        verify(repository, times(2)).findAll();
        verify(checkService, times(0)).validateCheck(anyString());
    }
}