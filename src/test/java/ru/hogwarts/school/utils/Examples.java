package ru.hogwarts.school.utils;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

public class Examples {

    public static final List<Student> STUDENTS = new ArrayList<>();
    public static final List<Faculty> FACULTIES = new ArrayList<>();

    public static final Student HARRY = new Student(1, "Гарри", 12);
    public static final Student RON = new Student(2, "Рон", 12);
    public static final Student HERMIONE = new Student(3, "Гермиона", 12);
    public static final Student DRACO = new Student(4, "Драко", 12);

    public static final int AGE = 12;

    public static final Student EDIT_STUDENT = new Student(HARRY.getId(), DRACO.getName(), DRACO.getAge());
    public static final Student INVALIDE_STUDENT = new Student(-1, null, 0);


    public static final Faculty GRIFFINDOR = new Faculty(1, "Гриффиндор", "красный");
    public static final Faculty SLYTHERIN = new Faculty(2, "Слизерин", "зеленый");
    public static final Faculty RAVENCLAW = new Faculty(3, "Когтевран", "синий");
    public static final Faculty HUFFLEPUFF = new Faculty(4, "Пуффендуй", "желтый");

    public static final String COLOR = "красный";

    public static final Faculty EDIT_FACULTY = new Faculty(GRIFFINDOR.getId(), HUFFLEPUFF.getName(), HUFFLEPUFF.getColor());
    public static final Faculty INVALIDE_FACULTY = new Faculty(-1, null, null);

    public static List<Student> getStudents() {
        STUDENTS.add(HARRY);
        STUDENTS.add(RON);
        STUDENTS.add(HERMIONE);
        return STUDENTS;
    }

    public static List<Faculty> getFaculties() {
        FACULTIES.add(GRIFFINDOR);
        FACULTIES.add(SLYTHERIN);
        FACULTIES.add(RAVENCLAW);
        return FACULTIES;
    }
}