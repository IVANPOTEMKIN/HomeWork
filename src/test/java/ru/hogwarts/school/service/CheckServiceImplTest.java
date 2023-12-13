package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import ru.hogwarts.school.exception.FacultyAlreadyAddedException;
import ru.hogwarts.school.exception.InvalideInputException;
import ru.hogwarts.school.exception.StudentAlreadyAddedException;
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
                Arguments.of((Object) null)
        );
    }

    public static Stream<Arguments> provideParamsForFacultyTest() {
        return Stream.of(
                Arguments.of((Object) null)
        );
    }

    public static Stream<Arguments> provideParamsForLongTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(INVALIDE_ID),
                Arguments.of(-1L)
        );
    }

    public static Stream<Arguments> provideParamsForIntegerTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(INVALIDE_AGE_STUDENT),
                Arguments.of(-1)
        );
    }

    public static Stream<Arguments> provideParamsForSeveralIntegerTest() {
        return Stream.of(
                Arguments.of(HERMIONE_AGE, HARRY_AGE)
        );
    }

    public static Stream<Arguments> provideParamsForStringTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(" "),
                Arguments.of(INVALIDE_NAME_FACULTY)
        );
    }

    public static Stream<Arguments> provideParamsForSeveralStringTest() {
        return Stream.of(
                Arguments.of(GRIFFINDOR_NAME, GRIFFINDOR_NAME)
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

    @Test
    void validateCheckLong_success() {
        assertFalse(service.validateCheck(HARRY_ID));
    }

    @Test
    void validateCheckInteger_success() {
        assertFalse(service.validateCheck(HARRY_AGE));
    }

    @Test
    void validateCheckSeveralInteger_success() {
        assertFalse(service.validateCheck(HARRY_AGE, HERMIONE_AGE));
    }

    @Test
    void validateCheckString_success() {
        assertFalse(service.validateCheck(HARRY_NAME));
    }

    @Test
    void validateCheckSeveralString_success() {
        assertFalse(service.validateCheck(GRIFFINDOR_NAME, GRIFFINDOR_COLOR));
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

    @ParameterizedTest
    @MethodSource("provideParamsForLongTest")
    void validateCheckLong_InvalideInputException(Long value) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Введены некорректные данные!";

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.validateCheck(value)
        );

        assertEquals(expected, actual.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForIntegerTest")
    void validateCheckInteger_InvalideInputException(Integer value) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Введены некорректные данные!";

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.validateCheck(value)
        );

        assertEquals(expected, actual.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForSeveralIntegerTest")
    void validateCheckSeveralInteger_InvalideInputException(Integer value1, Integer value2) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Введены некорректные данные!";

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.validateCheck(value1, value2)
        );

        assertEquals(expected, actual.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForStringTest")
    void validateCheckString_InvalideInputException(String str) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Введены некорректные данные!";

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.validateCheck(str)
        );

        assertEquals(expected, actual.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForSeveralStringTest")
    void validateCheckSeveralString_InvalideInputException(String str1, String str2) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String expected = status.value() + " Введены некорректные данные!";

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.validateCheck(str1, str2)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    void isStudentAlreadyAdded_success() {
        assertFalse(service.isStudentAlreadyAdded(getStudents(), RONALD));
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
}