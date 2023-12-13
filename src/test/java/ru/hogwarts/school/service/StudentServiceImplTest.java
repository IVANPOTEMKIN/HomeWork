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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
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

    private void getHarry() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(HARRY));

        HARRY.setId(HARRY_ID);
    }

    @Test
    void add_success() {
        when(repository.save(any(Student.class)))
                .thenReturn(HARRY);

        assertEquals(HARRY,
                studentService.add(
                        HARRY_NAME,
                        HARRY_AGE,
                        GRIFFINDOR_ID));
    }

    @Test
    void add_InvalideInputException() {
        when(repository.save(any(Student.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.add(
                        INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT,
                        INVALIDE_ID));
    }

    @Test
    void add_StudentAlreadyAddedException() {
        when(repository.save(any(Student.class)))
                .thenThrow(StudentAlreadyAddedException.class);

        assertThrows(StudentAlreadyAddedException.class,
                () -> studentService.add(
                        HARRY_NAME,
                        HARRY_AGE,
                        GRIFFINDOR_ID));
    }

    @Test
    void get_success() {
        getHarry();

        assertEquals(HARRY,
                studentService.get(HARRY.getId()));
    }

    @Test
    void get_InvalideInputException() {
        when(repository.findById(any(Long.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.get(INVALIDE_ID));
    }

    @Test
    void get_StudentNotFoundException() {
        when(repository.findById(any(Long.class)))
                .thenThrow(StudentNotFoundException.class);

        assertThrows(StudentNotFoundException.class,
                () -> studentService.get(HARRY_ID));
    }

    @Test
    void edit_WithOnlyName_success() {
        getHarry();

        when(repository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, HARRY_AGE, GRIFFINDOR));

        assertEquals(new Student(DRACO_NAME, HARRY_AGE, GRIFFINDOR),
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        null,
                        null));
    }

    @Test
    void edit_WithOnlyName_InvalideInputException() {
        getHarry();

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        null,
                        null));
    }

    @Test
    void edit_WithOnlyNameAndAge_success() {
        getHarry();

        when(repository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, DRACO_AGE, GRIFFINDOR));

        assertEquals(new Student(DRACO_NAME, DRACO_AGE, GRIFFINDOR),
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        DRACO_AGE,
                        null));
    }

    @Test
    void edit_WithOnlyNameAndAge_InvalideInputException() {
        getHarry();

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT,
                        null));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_success() {
        getHarry();

        when(repository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, HARRY_AGE, SLYTHERIN));

        SLYTHERIN.setId(SLYTHERIN_ID);

        assertEquals(new Student(DRACO_NAME, HARRY_AGE, SLYTHERIN),
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        null,
                        SLYTHERIN.getId()));
    }

    @Test
    void edit_WithOnlyNameAndFacultyId_InvalideInputException() {
        getHarry();

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        null,
                        INVALIDE_ID));
    }

    @Test
    void edit_WithOnlyAge_success() {
        getHarry();

        when(repository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, DRACO_AGE, GRIFFINDOR));

        assertEquals(new Student(HARRY_NAME, DRACO_AGE, GRIFFINDOR),
                studentService.edit(
                        HARRY.getId(),
                        null,
                        DRACO_AGE,
                        null));
    }

    @Test
    void edit_WithOnlyAge_InvalideInputException() {
        getHarry();

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        null,
                        INVALIDE_AGE_STUDENT,
                        null));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_success() {
        getHarry();

        when(repository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, DRACO_AGE, SLYTHERIN));

        SLYTHERIN.setId(SLYTHERIN_ID);

        assertEquals(new Student(HARRY_NAME, DRACO_AGE, SLYTHERIN),
                studentService.edit(
                        HARRY.getId(),
                        null,
                        DRACO_AGE,
                        SLYTHERIN.getId()));
    }

    @Test
    void edit_WithOnlyAgeAndFacultyId_InvalideInputException() {
        getHarry();

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        null,
                        INVALIDE_AGE_STUDENT,
                        INVALIDE_ID));
    }

    @Test
    void edit_WithOnlyFacultyId_success() {
        getHarry();

        when(repository.save(any(Student.class)))
                .thenReturn(new Student(HARRY_NAME, HARRY_AGE, SLYTHERIN));

        SLYTHERIN.setId(SLYTHERIN_ID);

        assertEquals(new Student(HARRY_NAME, HARRY_AGE, SLYTHERIN),
                studentService.edit(
                        HARRY.getId(),
                        null,
                        null,
                        SLYTHERIN.getId()));
    }

    @Test
    void edit_WithOnlyFacultyId_InvalideInputException() {
        getHarry();

        when(facultyService.get(any(Long.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        null,
                        null,
                        INVALIDE_ID));
    }

    @Test
    void edit_WithAllParameters_success() {
        getHarry();

        when(repository.save(any(Student.class)))
                .thenReturn(new Student(DRACO_NAME, DRACO_AGE, SLYTHERIN));

        SLYTHERIN.setId(SLYTHERIN_ID);

        assertEquals(new Student(DRACO_NAME, DRACO_AGE, SLYTHERIN),
                studentService.edit(
                        HARRY.getId(),
                        DRACO_NAME,
                        DRACO_AGE,
                        SLYTHERIN.getId()));
    }

    @Test
    void edit_WithAllParameters_InvalideInputException() {
        getHarry();

        assertThrows(InvalideInputException.class,
                () -> studentService.edit(
                        HARRY.getId(),
                        INVALIDE_NAME_STUDENT,
                        INVALIDE_AGE_STUDENT,
                        INVALIDE_ID));
    }

    @Test
    void edit_WithoutParameters_success() {
        getHarry();

        assertEquals(HARRY, studentService.edit(
                HARRY.getId(),
                null,
                null,
                null));
    }

    @Test
    void remove_success() {
        getHarry();

        assertEquals(HARRY,
                studentService.remove(HARRY.getId()));
    }

    @Test
    void getAll_success() {
        when(repository.findAll())
                .thenReturn(getStudents());

        assertEquals(getStudents(),
                studentService.getAll());
    }

    @Test
    void getByAge_WithOnlyFirstParameter_success() {
        when(repository.findByAge(any(Integer.class)))
                .thenReturn(List.of(HARRY));

        assertEquals(List.of(HARRY),
                studentService.getByAge(
                        HARRY_AGE,
                        null));
    }

    @Test
    void getByAge_WithOnlyFirstParameter_InvalideInputException() {
        when(repository.findByAge(any(Integer.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(
                        INVALIDE_AGE_STUDENT,
                        null));
    }

    @Test
    void getByAge_WithOnlySecondParameter_success() {
        when(repository.findByAge(any(Integer.class)))
                .thenReturn(List.of(HERMIONE));

        assertEquals(List.of(HERMIONE),
                studentService.getByAge(
                        null,
                        HERMIONE_AGE));
    }

    @Test
    void getByAge_WithOnlySecondParameter_InvalideInputException() {
        when(repository.findByAge(any(Integer.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(
                        null,
                        INVALIDE_AGE_STUDENT));
    }

    @Test
    void getByAge_WithAllParameters_success() {
        when(repository.findByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenReturn(getStudents());

        assertEquals(getStudents(),
                studentService.getByAge(
                        HARRY_AGE,
                        HERMIONE_AGE));
    }

    @Test
    void getByAge_WithAllParameters_InvalideInputException() {
        when(repository.findByAgeBetween(any(Integer.class), any(Integer.class)))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> studentService.getByAge(HERMIONE_AGE, HARRY_AGE));
    }

    @Test
    void getByAge_WithoutParameters_success() {
        when(repository.findAll())
                .thenReturn(getStudents());

        assertEquals(getStudents(),
                studentService.getByAge(
                        null,
                        null));
    }

    @Test
    void getFacultyById_success() {
        getHarry();

        GRIFFINDOR.setId(GRIFFINDOR_ID);
        HARRY.setFaculty(GRIFFINDOR);

        assertEquals(GRIFFINDOR,
                studentService.getFacultyById(HARRY.getId()));
    }

    @Test
    void getAmountAllFaculties_success() {
        when(repository.getAmountAllStudents())
                .thenReturn(AMOUNT_STUDENTS);

        assertEquals(AMOUNT_STUDENTS,
                studentService.getAmountAllStudents());
    }

    @Test
    void getAvgAgeStudents_success() {
        DecimalFormat numberFormat = new DecimalFormat("#.#");

        when(repository.getAvgAgeStudents())
                .thenReturn(AVG_AGE_STUDENTS);

        assertEquals(numberFormat.format(AVG_AGE_STUDENTS),
                studentService.getAvgAgeStudents());
    }

    @Test
    void getLastFiveStudents_success() {
        when(repository.getLastFiveStudents())
                .thenReturn(getStudents());

        assertEquals(getStudents(),
                studentService.getLastFiveStudents());
    }
}