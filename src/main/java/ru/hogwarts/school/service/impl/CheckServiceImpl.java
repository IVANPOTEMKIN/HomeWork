package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.CheckService;

import java.util.Map;

@Service
public class CheckServiceImpl implements CheckService {
    @Override
    public boolean validateCheck(Student student) {
        if (student == null
                || student.getName() == null
                || student.getName().isBlank()
                || !student.getName().matches("[а-яА-Я]+")
                || student.getAge() <= 0
                || student.getId() < 0) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(Faculty faculty) {
        if (faculty == null
                || faculty.getName() == null
                || faculty.getName().isBlank()
                || !faculty.getName().matches("[а-яА-Я]+")
                || faculty.getColor() == null
                || faculty.getColor().isBlank()
                || !faculty.getColor().matches("[а-яА-Я]+")
                || faculty.getId() < 0) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean isStudentAlreadyAdded(Map<Long, Student> students, Student student) {
        for (Student element : students.values()) {
            if (element.getName().equalsIgnoreCase(student.getName())
                    && element.getAge() == student.getAge()) {
                throw new StudentAlreadyAddedException();
            }
        }
        return false;
    }

    @Override
    public boolean isFacultyAlreadyAdded(Map<Long, Faculty> faculties, Faculty faculty) {
        for (Faculty element : faculties.values()) {
            if (element.getName().equalsIgnoreCase(faculty.getName())
                    && element.getColor().equalsIgnoreCase(faculty.getColor())) {
                throw new FacultyAlreadyAddedException();
            }
        }
        return false;
    }

    @Override
    public boolean isNotStudentContains(Map<Long, Student> students, long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException();
        }
        return false;
    }

    @Override
    public boolean isNotFacultyContains(Map<Long, Faculty> faculties, long id) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException();
        }
        return false;
    }
}