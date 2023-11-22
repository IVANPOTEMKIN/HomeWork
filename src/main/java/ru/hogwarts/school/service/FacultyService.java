package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface FacultyService {
    Faculty add(Faculty faculty);

    Faculty get(Long id);

    Faculty edit(Long id, Faculty faculty);

    void remove(Long id);

    Collection<Faculty> getAll();

    Collection<Faculty> getByNameOrColor(String name, String color);

    Collection<Student> getStudentsByFacultyId(Long id);
}