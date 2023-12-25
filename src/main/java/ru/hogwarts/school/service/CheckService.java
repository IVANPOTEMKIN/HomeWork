package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface CheckService {
    boolean validateCheck(Student student);

    boolean validateCheck(Faculty faculty);

    boolean validateCheck(Long value);

    boolean validateCheck(Integer value);

    boolean validateCheck(Integer minValue, Integer maxValue);

    boolean validateCheck(String str);

    boolean validateCheck(String str1, String str2);

    boolean isStudentAlreadyAdded(Collection<Student> students, Student student);

    boolean isFacultyAlreadyAdded(Collection<Faculty> faculties, Faculty faculty);
}