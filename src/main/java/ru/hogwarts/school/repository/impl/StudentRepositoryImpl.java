package ru.hogwarts.school.repository.impl;

import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.CheckService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository("studentRep")
public class StudentRepositoryImpl implements StudentRepository {

    private final Map<Long, Student> students;
    private static long counterId;
    private final CheckService service;

    public StudentRepositoryImpl(CheckService service) {
        students = new HashMap<>();
        counterId = 0;
        this.service = service;
    }

    @Override
    @PostConstruct
    public void init() {
        add(new Student(0, "Гарри", 12));
        add(new Student(0, "Рон", 12));
        add(new Student(0, "Гермиона", 12));
    }

    @Override
    public Student add(Student student) {
        service.validateCheck(student);
        service.isStudentAlreadyAdded(students, student);

        Student newStudent = new Student(++counterId, student.getName(), student.getAge());
        students.put(counterId, newStudent);
        return newStudent;
    }

    @Override
    public Student get(long id) {
        service.isNotStudentContains(students, id);

        return students.get(id);
    }

    @Override
    public Student edit(long id, Student student) {
        Student updateStudent = get(id);

        service.validateCheck(student);
        updateStudent.setName(student.getName());
        updateStudent.setAge(student.getAge());

        students.put(id, updateStudent);
        return updateStudent;
    }

    @Override
    public Student remove(long id) {
        service.isNotStudentContains(students, id);

        return students.remove(id);
    }

    @Override
    public Collection<Student> getAll() {
        return Collections.unmodifiableCollection(students.values());
    }

    @Override
    public Collection<Student> getByAge(int age) {
        return students.values()
                .stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toUnmodifiableList());
    }
}