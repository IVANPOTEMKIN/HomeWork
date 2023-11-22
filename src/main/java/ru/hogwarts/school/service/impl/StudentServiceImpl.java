package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.CheckService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final CheckService service;

    @Autowired
    public StudentServiceImpl(StudentRepository repository, CheckService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public Student add(Student student) {
        service.validateCheck(student);
        service.isStudentAlreadyAdded(getAll(), student);
        return repository.save(student);
    }

    @Override
    public Student get(long id) {
        return repository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public Student edit(long id, Student student) {
        Student updateStudent = get(id);

        service.validateCheck(student);
        updateStudent.setName(student.getName());
        updateStudent.setAge(student.getAge());

        return repository.save(updateStudent);
    }

    @Override
    public void remove(long id) {
        Student deleteStudent = get(id);
        repository.deleteById(deleteStudent.getId());
    }

    @Override
    public Collection<Student> getAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Student> getByAge(int age) {
        return repository.findAllByAge(age);
    }
}