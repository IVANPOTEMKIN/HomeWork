package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface CheckService {
    boolean validateCheck(Student student);

    boolean validateCheck(Faculty faculty);

    boolean validateCheck(Long value);

    boolean validateCheck(Long minValue, Long maxValue);

    boolean validateCheck(String str);

    boolean validateCheck(String strOne, String strTwo);

    boolean isStudentAlreadyAdded(Collection<Student> students, Student student);

    boolean isFacultyAlreadyAdded(Collection<Faculty> faculties, Faculty faculty);
}