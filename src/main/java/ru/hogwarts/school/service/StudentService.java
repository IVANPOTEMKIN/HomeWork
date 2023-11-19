package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student add(Student student);

    Student get(long id);

    Student edit(long id, Student student);

    Student remove(long id);

    Collection<Student> getAll();

    Collection<Student> getByAge(int age);
}