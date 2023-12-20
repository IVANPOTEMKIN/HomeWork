package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.CheckService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final FacultyService facultyService;
    private final CheckService checkService;
    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    public StudentServiceImpl(StudentRepository repository,
                              FacultyService facultyService,
                              CheckService checkService) {
        this.repository = repository;
        this.facultyService = facultyService;
        this.checkService = checkService;
    }

    @Override
    public Student add(String name, Integer age, Long facultyId) {
        logger.info("Вызван метод \"add({}, {}, {})\" сервиса \"Student\"", name, age, facultyId);

        Faculty faculty = facultyService.get(facultyId);
        Student newStudent = new Student(name, age, faculty);

        checkService.validateCheck(newStudent);
        checkService.isStudentAlreadyAdded(getAll(), newStudent);

        return repository.save(newStudent);
    }

    @Override
    public Student get(Long id) {
        logger.info("Вызван метод \"get({})\" сервиса \"Student\"", id);

        checkService.validateCheck(id);
        return repository.findById(id).orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public Student edit(Long id, String name, Integer age, Long facultyId) {
        logger.info("Вызван метод \"edit({}, {}, {}, {})\" сервиса \"Student\"", id, name, age, facultyId);

        Student student = get(id);

        if (name != null & age == null & facultyId == null) {
            Student newStudent = new Student(name, student.getAge(), student.getFaculty());
            newStudent.setId(student.getId());

            checkService.validateCheck(newStudent);
            checkService.isStudentAlreadyAdded(getAll(), newStudent);

            logger.info("Имя студента УСПЕШНО ИЗМЕНЕНО на \"{}\"", name);
            return repository.save(newStudent);
        }

        if (name != null & age != null & facultyId == null) {
            Student newStudent = new Student(name, age, student.getFaculty());
            newStudent.setId(student.getId());

            checkService.validateCheck(newStudent);
            checkService.isStudentAlreadyAdded(getAll(), newStudent);

            logger.info("Имя и возраст студента УСПЕШНО ИЗМЕНЕНЫ на \"{}, {}\"", name, age);
            return repository.save(newStudent);
        }

        if (name != null & age == null & facultyId != null) {
            Student newStudent = new Student(name, student.getAge(), new Faculty());
            newStudent.setFaculty(facultyService.get(facultyId));
            newStudent.setId(student.getId());

            checkService.validateCheck(newStudent);
            checkService.isStudentAlreadyAdded(getAll(), newStudent);

            logger.info("Имя и ID факультета студента УСПЕШНО ИЗМЕНЕНЫ на \"{}, {}\"", name, facultyId);
            return repository.save(newStudent);
        }

        if (name == null & age != null & facultyId == null) {
            Student newStudent = new Student(student.getName(), age, student.getFaculty());
            newStudent.setId(student.getId());

            checkService.validateCheck(newStudent);
            checkService.isStudentAlreadyAdded(getAll(), newStudent);

            logger.info("Возраст студента УСПЕШНО ИЗМЕНЕН на \"{}\"", age);
            return repository.save(newStudent);
        }

        if (name == null & age != null & facultyId != null) {
            Student newStudent = new Student(student.getName(), age, new Faculty());
            newStudent.setFaculty(facultyService.get(facultyId));
            newStudent.setId(student.getId());

            checkService.validateCheck(newStudent);
            checkService.isStudentAlreadyAdded(getAll(), newStudent);

            logger.info("Возраст и ID факультета студента УСПЕШНО ИЗМЕНЕНЫ на \"{}, {}\"", age, facultyId);
            return repository.save(newStudent);
        }

        if (name == null & age == null & facultyId != null) {
            Student newStudent = new Student(student.getName(), student.getAge(), new Faculty());
            newStudent.setFaculty(facultyService.get(facultyId));
            newStudent.setId(student.getId());

            logger.info("ID факультета студента УСПЕШНО ИЗМЕНЕН на \"{}\"", facultyId);
            return repository.save(newStudent);
        }

        if (name != null & age != null & facultyId != null) {
            Student newStudent = new Student(name, age, new Faculty());
            newStudent.setFaculty(facultyService.get(facultyId));
            newStudent.setId(student.getId());

            checkService.validateCheck(newStudent);
            checkService.isStudentAlreadyAdded(getAll(), newStudent);

            logger.info("Имя, возраст и ID факультета студента УСПЕШНО ИЗМЕНЕНЫ на \"{}, {}, {}\"", name, age, facultyId);
            return repository.save(newStudent);
        }

        logger.info("Изменения НЕ БЫЛИ ВНЕСЕНЫ");
        return student;
    }

    @Override
    public Student remove(Long id) {
        logger.info("Вызван метод \"remove({})\" сервиса \"Student\"", id);

        Student deleteStudent = get(id);
        repository.delete(deleteStudent);

        logger.info("Студент \"{}\" УСПЕШНО УДАЛЕН", id);
        return deleteStudent;
    }

    @Override
    public Collection<Student> getAll() {
        logger.info("Вызван метод \"getAll()\" сервиса \"Student\"");
        logger.info("Список всех студентов УСПЕШНО ПОЛУЧЕН");
        return repository.findAll();
    }

    @Override
    public Collection<Student> getByAge(Integer minAge, Integer maxAge) {
        logger.info("Вызван метод \"getByAge({}, {})\" сервиса \"Student\"", minAge, maxAge);

        if (minAge != null & maxAge == null) {
            checkService.validateCheck(minAge);
            logger.info("Список всех студентов возрастом \"{}\" УСПЕШНО ПОЛУЧЕН", minAge);
            return repository.findByAge(minAge);
        }

        if (minAge == null & maxAge != null) {
            checkService.validateCheck(maxAge);
            logger.info("Список всех студентов возрастом \"{}\" УСПЕШНО ПОЛУЧЕН", maxAge);
            return repository.findByAge(maxAge);
        }

        if (minAge != null & maxAge != null) {
            checkService.validateCheck(minAge, maxAge);
            logger.info("Список всех студентов возрастом \"{} - {}\" УСПЕШНО ПОЛУЧЕН", minAge, maxAge);
            return repository.findByAgeBetween(minAge, maxAge);
        }

        return getAll();
    }

    @Override
    public Collection<Student> getByName(String name) {
        logger.info("Вызван метод \"getByName\" сервиса \"Student\"");

        if (name != null) {
            checkService.validateCheck(name);
            logger.info("Список всех студентов с именем \"{}\" УСПЕШНО ПОЛУЧЕН", name);
            return repository.findByNameContainsIgnoreCase(name);
        }

        return getAll();
    }

    @Override
    public Faculty getFacultyById(Long id) {
        logger.info("Вызван метод \"getFacultyById\" сервиса \"Student\"");
        logger.info("Факультет студента \"{}\" УСПЕШНО ПОЛУЧЕН", id);
        return get(id).getFaculty();
    }

    @Override
    public Integer getAmountAllStudents() {
        logger.info("Вызван метод \"getAmountAllStudents\" сервиса \"Student\"");
        logger.info("Количество студентов УСПЕШНО ПОЛУЧЕНО");
        return repository.getAmountAllStudents();
    }

    @Override
    public String getAvgAgeStudents() {
        logger.info("Вызван метод \"getAvgAgeStudents\" сервиса \"Student\"");
        logger.info("Средний возраст студентов УСПЕШНО ПОЛУЧЕН");
        DecimalFormat numberFormat = new DecimalFormat("#.#");
        return numberFormat.format(repository.getAvgAgeStudents());
    }

    @Override
    public Collection<Student> getLastFiveStudents() {
        logger.info("Вызван метод \"getLastFiveStudents\" сервиса \"Student\"");
        logger.info("Список последних 5 студентов УСПЕШНО ПОЛУЧЕН");
        return repository.getLastFiveStudents();
    }

    @Override
    public Collection<String> getBySortedName(String prefix) {
        if (prefix != null) {
            checkService.validateCheck(prefix);
            return getAll().stream()
                    .map(Student::getName)
                    .map(String::toUpperCase)
                    .filter(s -> StringUtils.startsWithIgnoreCase(s, prefix))
                    .sorted()
                    .collect(Collectors.toUnmodifiableList());
        }

        return getAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toUnmodifiableList());
    }
}