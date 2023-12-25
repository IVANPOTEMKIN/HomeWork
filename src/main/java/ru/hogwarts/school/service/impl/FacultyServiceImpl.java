package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.CheckService;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;
    private final CheckService checkService;
    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Autowired
    public FacultyServiceImpl(FacultyRepository repository, CheckService checkService) {
        this.repository = repository;
        this.checkService = checkService;
    }

    @Override
    public Faculty add(String name, String color) {
        logger.info("Вызван метод \"add({}, {})\" сервиса \"Faculty\"", name, color);

        Faculty newFaculty = new Faculty(name, color);

        checkService.validateCheck(newFaculty);
        checkService.isFacultyAlreadyAdded(getAll(), newFaculty);

        return repository.save(newFaculty);
    }

    @Override
    public Faculty get(Long id) {
        logger.info("Вызван метод \"get({})\" сервиса \"Faculty\"", id);

        checkService.validateCheck(id);
        return repository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public Faculty edit(Long id, String name, String color) {
        logger.info("Вызван метод \"edit({}, {}, {})\" сервиса \"Faculty\"", id, name, color);

        Faculty faculty = get(id);

        if (name != null & color == null) {
            Faculty newFaculty = new Faculty(name, faculty.getColor());
            newFaculty.setId(faculty.getId());

            checkService.validateCheck(newFaculty);
            checkService.isFacultyAlreadyAdded(getAll(), newFaculty);

            logger.info("Название факультета УСПЕШНО ИЗМЕНЕНО на \"{}\"", name);
            return repository.save(newFaculty);
        }

        if (name == null & color != null) {
            Faculty newFaculty = new Faculty(faculty.getName(), color);
            newFaculty.setId(faculty.getId());

            checkService.validateCheck(newFaculty);
            checkService.isFacultyAlreadyAdded(getAll(), newFaculty);

            logger.info("Цвет факультета УСПЕШНО ИЗМЕНЕН на \"{}\"", color);
            return repository.save(newFaculty);
        }

        if (name != null & color != null) {
            Faculty newFaculty = new Faculty(name, color);
            newFaculty.setId(faculty.getId());

            checkService.validateCheck(newFaculty);
            checkService.isFacultyAlreadyAdded(getAll(), newFaculty);

            logger.info("Название и цвет факультета УСПЕШНО ИЗМЕНЕНЫ на \"{}, {}\"", name, color);
            return repository.save(newFaculty);
        }

        logger.info("Изменения НЕ БЫЛИ ВНЕСЕНЫ");
        return faculty;
    }

    @Override
    public Faculty remove(Long id) {
        logger.info("Вызван метод \"remove({})\" сервиса \"Faculty\"", id);

        Faculty deleteFaculty = get(id);
        repository.delete(deleteFaculty);

        logger.info("Факультет \"{}\" УСПЕШНО УДАЛЕН", id);
        return deleteFaculty;
    }

    @Override
    public Collection<Faculty> getAll() {
        logger.info("Вызван метод \"getAll()\" сервиса \"Faculty\"");
        logger.info("Список всех факультетов УСПЕШНО ПОЛУЧЕН");
        return repository.findAll();
    }

    @Override
    public Collection<Faculty> getByNameOrColor(String name, String color) {
        logger.info("Вызван метод \"getByNameOrColor({}, {})\" сервиса \"Faculty\"", name, color);

        if (name != null & color == null) {
            checkService.validateCheck(name);
            logger.info("Факультет с названием \"{}\" УСПЕШНО ПОЛУЧЕН", name);
            return repository.findByNameContainsIgnoreCase(name);
        }

        if (name == null & color != null) {
            checkService.validateCheck(color);
            logger.info("Факультет с цветом \"{}\" УСПЕШНО ПОЛУЧЕН", color);
            return repository.findByColorContainsIgnoreCase(color);
        }

        if (name != null & color != null) {
            checkService.validateCheck(name, color);
            logger.info("Факультет с названием и цветом \"{}, {}\" УСПЕШНО ПОЛУЧЕН", name, color);
            return repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(name, color);
        }

        return getAll();
    }

    @Override
    public Collection<Student> getStudentsByFacultyId(Long id) {
        logger.info("Вызван метод \"getStudentsByFacultyId({})\" сервиса \"Faculty\"", id);
        logger.info("Список всех студентов факультета \"{}\" УСПЕШНО ПОЛУЧЕН", id);
        return get(id).getStudents();
    }

    @Override
    public Integer getAmountAllFaculties() {
        logger.info("Вызван метод \"getAmountAllFaculties()\" сервиса \"Faculty\"");
        logger.info("Количество факультетов УСПЕШНО ПОЛУЧЕНО");
        return repository.getAmountAllFaculties();
    }

    @Override
    public String getLongestName() {
        logger.info("Вызван метод \"getLongestName()\" сервиса \"Faculty\"");
        logger.info("Самое длинное название факультета УСПЕШНО ПОЛУЧЕНО");

        return getAll().stream()
                .map(Faculty::getName)
                .map(String::toUpperCase)
                .max(Comparator.comparing(String::length))
                .orElseThrow(FacultyNotFoundException::new);
    }
}