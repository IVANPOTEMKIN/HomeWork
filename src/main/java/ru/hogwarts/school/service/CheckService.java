package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface CheckService {
    boolean validateCheck(Student student);

    boolean validateCheck(Faculty faculty);

    boolean isStudentAlreadyAdded(Collection<Student> students, Student student);

    boolean isFacultyAlreadyAdded(Collection<Faculty> faculties, Faculty faculty);

    boolean isNotStudentContains(Collection<Student> students, long id);

    boolean isNotFacultyContains(Collection<Faculty> faculties, long id);
}