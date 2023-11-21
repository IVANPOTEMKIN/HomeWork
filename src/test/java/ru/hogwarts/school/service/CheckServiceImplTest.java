package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.impl.CheckServiceImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static ru.hogwarts.school.utils.Examples.*;

public class CheckServiceImplTest {

    private final CheckService service = new CheckServiceImpl();

    public static Stream<Arguments> provideParamsForStudentTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(new Student(HARRY.getId(), null, HARRY.getAge())),
                Arguments.of(new Student(HARRY.getId(), " ", HARRY.getAge())),
                Arguments.of(new Student(HARRY.getId(), "Гарри_1", HARRY.getAge())),
                Arguments.of(new Student(-1, HARRY.getName(), HARRY.getAge())),
                Arguments.of(new Student(HARRY.getId(), HARRY.getName(), 0))
        );
    }

    public static Stream<Arguments> provideParamsForFacultyTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(new Faculty(GRIFFINDOR.getId(), null, GRIFFINDOR.getColor())),
                Arguments.of(new Faculty(GRIFFINDOR.getId(), " ", GRIFFINDOR.getColor())),
                Arguments.of(new Faculty(GRIFFINDOR.getId(), "Гриффиндор_1", GRIFFINDOR.getColor())),
                Arguments.of(new Faculty(GRIFFINDOR.getId(), GRIFFINDOR.getName(), null)),
                Arguments.of(new Faculty(GRIFFINDOR.getId(), GRIFFINDOR.getName(), " ")),
                Arguments.of(new Faculty(GRIFFINDOR.getId(), GRIFFINDOR.getName(), "красный_1")),
                Arguments.of(new Faculty(-1, GRIFFINDOR.getName(), "красный_1"))
        );
    }

    @Test
    void validateCheckStudent_success() {
        assertFalse(service.validateCheck(HARRY));
    }

    @Test
    void validateCheckFaculty_success() {
        assertFalse(service.validateCheck(GRIFFINDOR));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForStudentTest")
    void validateCheckStudent_InvalideInputException(Student student) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Введены некорректные данные!";

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.validateCheck(student)
        );

        assertEquals(expected, actual.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForFacultyTest")
    void validateCheckFaculty_InvalideInputException(Faculty faculty) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Введены некорректные данные!";

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.validateCheck(faculty)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void isStudentAlreadyAdded_success() {
        assertFalse(service.isStudentAlreadyAdded(getStudents(), DRACO));
    }

    @Test
    void isFacultyAlreadyAdded_success() {
        assertFalse(service.isFacultyAlreadyAdded(getFaculties(), HUFFLEPUFF));
    }

    @Test
    void isStudentAlreadyAdded_StudentAlreadyAddedException() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Студент уже добавлен!";

        Exception actual = assertThrows(
                StudentAlreadyAddedException.class,
                () -> service.isStudentAlreadyAdded(getStudents(), HARRY)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void isFacultyAlreadyAdded_FacultyAlreadyAddedException() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Факультет уже добавлен!";

        Exception actual = assertThrows(
                FacultyAlreadyAddedException.class,
                () -> service.isFacultyAlreadyAdded(getFaculties(), GRIFFINDOR)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void isNotStudentContains_success() {
        assertFalse(service.isNotStudentContains(getStudents(), HARRY.getId()));
    }

    @Test
    void isNotFacultyContains_success() {
        assertFalse(service.isNotFacultyContains(getFaculties(), GRIFFINDOR.getId()));
    }

    @Test
    void isNotStudentContains_StudentNotFoundException() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String expected = status.value() + " Студент не найден!";

        Exception actual = assertThrows(
                StudentNotFoundException.class,
                () -> service.isNotStudentContains(getStudents(), DRACO.getId())
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void isNotFacultyContains_FacultyNotFoundException() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String expected = status.value() + " Факультет не найден!";

        Exception actual = assertThrows(
                FacultyNotFoundException.class,
                () -> service.isNotFacultyContains(getFaculties(), HUFFLEPUFF.getId())
        );

        assertEquals(expected, actual.getMessage());
    }
}