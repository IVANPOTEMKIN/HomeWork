package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@Service("studentService")
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;

    public StudentServiceImpl(@Qualifier("studentRep") StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student add(Student student) {
        return repository.add(student);
    }

    @Override
    public Student get(long id) {
        return repository.get(id);
    }

    @Override
    public Student edit(long id, Student student) {
        return repository.edit(id, student);
    }

    @Override
    public Student remove(long id) {
        return repository.remove(id);
    }

    @Override
    public Collection<Student> getAll() {
        return repository.getAll();
    }

    @Override
    public Collection<Student> getByAge(int age) {
        return repository.getByAge(age);
    }
}