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
                || student.getName() == null
                || student.getName().isBlank()
                || !student.getName().matches("[а-яА-Я ]+")
                || student.getAge() == null
                || student.getAge() <= 0
                || student.getId() == null
                || student.getId() < 0
                || validateCheck(student.getFaculty())) {
            throw new InvalideInputException();
        }
        return false;
    }

    @Override
    public boolean validateCheck(Faculty faculty) {
        if (faculty == null
                || faculty.getName() == null
                || faculty.getName().isBlank()
                || !faculty.getName().matches("[а-яА-Я ]+")
                || faculty.getColor() == null
                || faculty.getColor().isBlank()
                || !faculty.getColor().matches("[а-яА-Я -]+")
                || faculty.getName().equalsIgnoreCase(faculty.getColor())
                || faculty.getId() == null
                || faculty.getId() < 0
                || faculty.getStudents() == null) {
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
        if (minValue == null
                || minValue <= 0
                || maxValue == null
                || maxValue <= 0
                || maxValue <= minValue) {
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
        if (str1 == null
                || str1.isBlank()
                || !str1.matches("[а-яА-Я -]+")
                || str2 == null
                || str2.isBlank()
                || !str2.matches("[а-яА-Я -]+")
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