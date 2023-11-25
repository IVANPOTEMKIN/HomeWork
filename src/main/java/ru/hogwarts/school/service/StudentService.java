package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student add(Student student);

    Student get(Long id);

    Student edit(Long id, Student student);

    Student remove(Long id);

    Collection<Student> getAll();

    Collection<Student> getByAge(Integer minAge, Integer maxAge);

    Faculty getFacultyById(Long id);
}