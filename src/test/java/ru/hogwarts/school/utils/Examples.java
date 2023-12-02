package ru.hogwarts.school.utils;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public class Examples {

    public static final Long HARRY_ID = 1L;

    public static final Student HARRY = new Student("Гарри Поттер", 11, new Faculty());
    public static final Student HERMIONE = new Student("Гермиона Грейнджер", 12, new Faculty());
    public static final Student DRACO = new Student("Драко Малфой", 11, new Faculty());


    public static final Long GRIFFINDOR_ID = 1L;
    public static final Long SLYTHERIN_ID = 2L;

    public static final Faculty GRIFFINDOR = new Faculty("Гриффиндор", "красно-золотой");
    public static final Faculty HUFFLEPUFF = new Faculty("Пуффендуй", "черно-желтый");

    public static final Faculty EDIT_FACULTY = new Faculty(HUFFLEPUFF.getName(), HUFFLEPUFF.getColor());


    public static List<Student> getStudents() {
        return List.of(HARRY, HERMIONE);
    }

    public static List<Faculty> getFaculties() {
        return List.of(GRIFFINDOR);
    }
}