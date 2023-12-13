package ru.hogwarts.school.utils;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public class Examples {

    public static final Long HARRY_ID = 1L;
    public static final String HARRY_NAME = "Гарри Поттер";
    public static final Integer HARRY_AGE = 11;

    public static final String HERMIONE_NAME = "Гермиона Грейнджер";
    public static final Integer HERMIONE_AGE = 12;

    public static final String RONALD_NAME = "Рональд Уизли";
    public static final Integer RONALD_AGE = 11;

    public static final String DRACO_NAME = "Драко Малфой";
    public static final Integer DRACO_AGE = 13;

    public static final Student HARRY = new Student(HARRY_NAME, HARRY_AGE, new Faculty());
    public static final Student HERMIONE = new Student(HERMIONE_NAME, HERMIONE_AGE, new Faculty());
    public static final Student RONALD = new Student(RONALD_NAME, RONALD_AGE, new Faculty());
    public static final Student DRACO = new Student(DRACO_NAME, DRACO_AGE, new Faculty());


    public static final Long GRIFFINDOR_ID = 1L;
    public static final String GRIFFINDOR_NAME = "Гриффиндор";
    public static final String GRIFFINDOR_COLOR = "Красный";

    public static final Long SLYTHERIN_ID = 2L;
    public static final String SLYTHERIN_NAME = "Слизерин";
    public static final String SLYTHERIN_COLOR = "Зеленый";

    public static final String HUFFLEPUFF_NAME = "Пуффендуй";
    public static final String HUFFLEPUFF_COLOR = "Желтый";

    public static final Faculty GRIFFINDOR = new Faculty(GRIFFINDOR_NAME, GRIFFINDOR_COLOR);
    public static final Faculty SLYTHERIN = new Faculty(SLYTHERIN_NAME, SLYTHERIN_COLOR);
    public static final Faculty HUFFLEPUFF = new Faculty(HUFFLEPUFF_NAME, HUFFLEPUFF_COLOR);


    public static List<Student> getStudents() {
        return List.of(HARRY, HERMIONE);
    }

    public static List<Faculty> getFaculties() {
        return List.of(GRIFFINDOR);
    }


    public static final Long INVALIDE_ID = 0L;

    public static final String INVALIDE_NAME_STUDENT = "Гарри_Поттер_1";
    public static final Integer INVALIDE_AGE_STUDENT = 0;

    public static final String INVALIDE_NAME_FACULTY = "Гриффиндор_1";
    public static final String INVALIDE_COLOR_FACULTY = "Красный_1";

    public static final String MESSAGE_INVALIDE_DATES = "Code: 400 BAD_REQUEST. Error: ВВЕДЕНЫ НЕКОРРЕКТНЫЕ ДАННЫЕ!";
    public static final String MESSAGE_FACULTY_NOT_FOUND = "Code: 404 NOT_FOUND. Error: ФАКУЛЬТЕТ НЕ НАЙДЕН!";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Code: 404 NOT_FOUND. Error: СТУДЕНТ НЕ НАЙДЕН!";

    public static final Integer AMOUNT_FACULTIES = 4;
    public static final Integer AMOUNT_STUDENTS = 15;
    public static final Double AVG_AGE_STUDENTS = 13.3;
}