package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Map;

public interface CheckService {
    boolean validateCheck(Student student);

    boolean validateCheck(Faculty faculty);

    boolean isStudentAlreadyAdded(Map<Long, Student> students, Student student);

    boolean isFacultyAlreadyAdded(Map<Long, Faculty> faculties, Faculty faculty);

    boolean isNotStudentContains(Map<Long, Student> students, long id);

    boolean isNotFacultyContains(Map<Long, Faculty> faculties, long id);
}