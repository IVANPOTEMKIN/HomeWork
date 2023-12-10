package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface FacultyService {
    Faculty add(String name, String color);

    Faculty get(Long id);

    Faculty edit(Long id, String name, String color);

    Faculty remove(Long id);

    Collection<Faculty> getAll();

    Collection<Faculty> getByNameOrColor(String name, String color);

    Collection<Student> getStudentsByFacultyId(Long id);

    Integer getAmountAllFaculties();
}