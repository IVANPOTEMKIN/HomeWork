package ru.hogwarts.school.utils;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;

public class Examples {

    public static final Map<Long, Student> STUDENTS = new HashMap<>();
    public static final Map<Long, Faculty> FACULTIES = new HashMap<>();

    public static final Student GARRY = new Student(1, "Гарри", 12);
    public static final Student RON = new Student(2, "Рон", 12);
    public static final Student HERMIONE = new Student(3, "Гермиона", 12);
    public static final Student DRACO = new Student(4, "Драко", 12);

    public static final Faculty GRIFFINDOR = new Faculty(1, "Гриффиндор", "красный");
    public static final Faculty SLYTHERIN = new Faculty(2, "Слизерин", "зеленый");
    public static final Faculty RAVENCLAW = new Faculty(3, "Когтевран", "синий");
    public static final Faculty HUFFLEPUFF = new Faculty(4, "Пуффендуй", "желтый");

    public static Map<Long, Student> getStudents() {
        STUDENTS.put(GARRY.getId(), GARRY);
        STUDENTS.put(RON.getId(), RON);
        STUDENTS.put(HERMIONE.getId(), HERMIONE);
        return STUDENTS;
    }

    public static Map<Long, Faculty> getFaculties() {
        FACULTIES.put(GRIFFINDOR.getId(), GRIFFINDOR);
        FACULTIES.put(SLYTHERIN.getId(), SLYTHERIN);
        FACULTIES.put(RAVENCLAW.getId(), RAVENCLAW);
        return FACULTIES;
    }
}