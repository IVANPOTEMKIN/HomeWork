package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student add(String name, Integer age, Long facultyId);

    Student get(Long id);

    Student edit(Long id, String name, Integer age, Long facultyId);

    Student remove(Long id);

    Collection<Student> getAll();

    Collection<Student> getByAge(Integer minAge, Integer maxAge);

    Collection<Student> getByName(String name);

    Faculty getFacultyById(Long id);

    Integer getAmountAllStudents();

    String getAvgAgeStudents();

    Collection<Student> getLastFiveStudents();

    Collection<String> getBySortedName(String prefix);

    Double getAvgAgeAllStudents();

    void getNames();

    void getNamesWithSynchronizedThread();
}