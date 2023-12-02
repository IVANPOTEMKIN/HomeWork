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
    public Student add(String name, Integer age, Long facultyId) {
        Student newStudent = new Student(name, age, new Faculty());
        newStudent.getFaculty().setId(facultyId);
        service.validateCheck(newStudent);
        service.isStudentAlreadyAdded(getAll(), newStudent);
        return repository.save(newStudent);
    }

    @Override
    public Student get(Long id) {
        service.validateCheck(id);
        return repository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public Student edit(Long id, String name, Integer age, Long facultyId) {
        Student updateStudent = get(id);

        if (name != null & age == null & facultyId == null) {
            service.validateCheck(name);
            updateStudent.setName(name);
            return repository.save(updateStudent);
        }

        if (name != null & age != null & facultyId == null) {
            service.validateCheck(name);
            service.validateCheck(age);
            updateStudent.setName(name);
            updateStudent.setAge(age);
            return repository.save(updateStudent);
        }

        if (name != null & age == null & facultyId != null) {
            service.validateCheck(name);
            service.validateCheck(facultyId);
            updateStudent.setName(name);
            updateStudent.setFaculty(new Faculty());
            updateStudent.getFaculty().setId(facultyId);
            return repository.save(updateStudent);
        }

        if (name == null & age != null & facultyId == null) {
            service.validateCheck(age);
            updateStudent.setAge(age);
            return repository.save(updateStudent);
        }

        if (name == null & age != null & facultyId != null) {
            service.validateCheck(age);
            service.validateCheck(facultyId);
            updateStudent.setAge(age);
            updateStudent.setFaculty(new Faculty());
            updateStudent.getFaculty().setId(facultyId);
            return repository.save(updateStudent);
        }

        if (name == null & age == null & facultyId != null) {
            service.validateCheck(facultyId);
            updateStudent.setFaculty(new Faculty());
            updateStudent.getFaculty().setId(facultyId);
            return repository.save(updateStudent);
        }

        if (name == null & age == null & facultyId == null) {
            return updateStudent;
        }

        service.validateCheck(name);
        service.validateCheck(age);
        service.validateCheck(facultyId);
        updateStudent.setName(name);
        updateStudent.setAge(age);
        updateStudent.setFaculty(new Faculty());
        updateStudent.getFaculty().setId(facultyId);
        return repository.save(updateStudent);
    }

    @Override
    public Student remove(Long id) {
        Student deleteStudent = get(id);
        repository.delete(deleteStudent);
        return deleteStudent;
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