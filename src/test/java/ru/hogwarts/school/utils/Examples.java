package ru.hogwarts.school.utils;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

public class Examples {

    public static final List<Student> STUDENTS = new ArrayList<>();
    public static final List<Faculty> FACULTIES = new ArrayList<>();

    public static final Student HARRY = new Student(1L, "Гарри Поттер", 12);
    public static final Student RON = new Student(2L, "Рон Уизли", 12);
    public static final Student HERMIONE = new Student(3L, "Гермиона Грейнджер", 13);
    public static final Student DRACO = new Student(4L, "Драко Малфой", 13);

    public static final Student EDIT_STUDENT = new Student(HARRY.getId(), DRACO.getName(), DRACO.getAge());
    public static final Student INVALIDE_STUDENT = new Student(-1L, null, 0);


    public static final Faculty GRIFFINDOR = new Faculty(1L, "Гриффиндор", "красно-золотой");
    public static final Faculty SLYTHERIN = new Faculty(2L, "Слизерин", "зеленый и серебряный");
    public static final Faculty RAVENCLAW = new Faculty(3L, "Когтевран", "синий и коричневый");
    public static final Faculty HUFFLEPUFF = new Faculty(4L, "Пуффендуй", "черно-желтый");

    public static final Faculty EDIT_FACULTY = new Faculty(GRIFFINDOR.getId(), HUFFLEPUFF.getName(), HUFFLEPUFF.getColor());
    public static final Faculty INVALIDE_FACULTY = new Faculty(-1L, null, null);

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