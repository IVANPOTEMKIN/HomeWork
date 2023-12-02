package ru.hogwarts.school.service.impl;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyAlreadyAddedException;
import ru.hogwarts.school.exception.InvalideInputException;
import ru.hogwarts.school.exception.StudentAlreadyAddedException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.CheckService;

import java.util.Collection;

@Service
public class CheckServiceImpl implements CheckService {
    @Override
    public boolean validateCheck(Student student) {
        if (student == null
                || validateCheck(student.getName())
                || validateCheck(student.getAge())
                || validateCheck(student.getFaculty())) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(Faculty faculty) {
        if (faculty == null
                || validateCheck(faculty.getName(), faculty.getColor())) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(Long value) {
        if (value == null || value <= 0) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(Integer value) {
        if (value == null || value <= 0) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(Integer minValue, Integer maxValue) {
        if (validateCheck(minValue)
                || validateCheck(maxValue)
                || maxValue < minValue) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(String str) {
        if (str == null
                || str.isBlank()
                || !str.matches("[а-яА-Я -]+")) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(String str1, String str2) {
        if (validateCheck(str1)
                || validateCheck(str2)
                || str1.equalsIgnoreCase(str2)) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean isStudentAlreadyAdded(Collection<Student> students, Student student) {
        for (Student element : students) {
            if (element.getName().equalsIgnoreCase(student.getName())
                    && element.getAge().equals(student.getAge())) {
                throw new StudentAlreadyAddedException();
            }
        }
        return false;
    }

    @Override
    public boolean isFacultyAlreadyAdded(Collection<Faculty> faculties, Faculty faculty) {
        for (Faculty element : faculties) {
            if (element.getName().equalsIgnoreCase(faculty.getName())
                    && element.getColor().equalsIgnoreCase(faculty.getColor())) {
                throw new FacultyAlreadyAddedException();
            }
        }
        return false;
    }
}