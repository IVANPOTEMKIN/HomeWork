package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
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
    public Student get(Long id) {
        service.validateCheck(id);
        return repository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public Student edit(Long id, Student student) {
        Student updateStudent = get(id);

        service.validateCheck(student);
        updateStudent.setName(student.getName());
        updateStudent.setAge(student.getAge());
        updateStudent.setFaculty(student.getFaculty());

        return repository.save(updateStudent);
    }

    @Override
    public void remove(Long id) {
        Student deleteStudent = get(id);
        repository.deleteById(deleteStudent.getId());
    }

    @Override
    public Collection<Student> getAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Student> getByAge(Integer minAge, Integer maxAge) {
        if (minAge != null & maxAge == null) {
            service.validateCheck(minAge);
            return repository.findByAge(minAge);
        }
        if (minAge == null & maxAge != null) {
            service.validateCheck(maxAge);
            return repository.findByAge(maxAge);
        }
        if (minAge != null & maxAge != null) {
            service.validateCheck(minAge, maxAge);
            return repository.findByAgeBetween(minAge, maxAge);
        }
        return repository.findAll();
    }

    @Override
    public Faculty getFacultyById(Long id) {
        return get(id).getFaculty();
    }
}