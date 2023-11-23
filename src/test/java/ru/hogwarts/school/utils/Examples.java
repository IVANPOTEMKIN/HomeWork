package ru.hogwarts.school.utils;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

public class Examples {

    public static final List<Student> STUDENTS = new ArrayList<>();

    public static final Student HARRY = new Student(1L, "Гарри Поттер", 12, new Faculty());
    public static final Student RON = new Student(2L, "Рон Уизли", 12, new Faculty());
    public static final Student HERMIONE = new Student(3L, "Гермиона Грейнджер", 13, new Faculty());
    public static final Student DRACO = new Student(4L, "Драко Малфой", 13, new Faculty());

    public static final Student EDIT_STUDENT = new Student(HARRY.getId(), DRACO.getName(), DRACO.getAge(), new Faculty());
    public static final Student INVALIDE_STUDENT = new Student(null, null, null, null);


    public static final List<Faculty> FACULTIES = new ArrayList<>();

    public static final Faculty GRIFFINDOR = new Faculty(1L, "Гриффиндор", "красно-золотой", List.of(HARRY, RON, HERMIONE));
    public static final Faculty SLYTHERIN = new Faculty(2L, "Слизерин", "зеленый и серебряный", List.of(DRACO));
    public static final Faculty RAVENCLAW = new Faculty(3L, "Когтевран", "синий и коричневый", List.of());
    public static final Faculty HUFFLEPUFF = new Faculty(4L, "Пуффендуй", "черно-желтый", List.of());

    public static final Faculty EDIT_FACULTY = new Faculty(GRIFFINDOR.getId(), HUFFLEPUFF.getName(), HUFFLEPUFF.getColor(), HUFFLEPUFF.getStudents());
    public static final Faculty INVALIDE_FACULTY = new Faculty(null, null, null, List.of(INVALIDE_STUDENT));


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