package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student add(Student student);

    Student get(Long id);

    Student edit(Long id, Student student);

    void remove(Long id);

    Collection<Student> getAll();

    Collection<Student> getByAge(Integer age);
}