package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.CheckService;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;
    private final CheckService service;

    @Autowired
    public FacultyServiceImpl(FacultyRepository repository, CheckService service) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public Faculty add(String name, String color) {
        Faculty newFaculty = new Faculty(name, color);
        service.validateCheck(newFaculty);
        service.isFacultyAlreadyAdded(getAll(), newFaculty);
        return repository.save(newFaculty);
    }

    @Override
    public Faculty get(Long id) {
        service.validateCheck(id);
        return repository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public Faculty edit(Long id, String name, String color) {
        Faculty updateFaculty = get(id);

        if (name != null & color == null) {
            service.validateCheck(name);
            updateFaculty.setName(name);
            return repository.save(updateFaculty);
        }

        if (name == null & color != null) {
            service.validateCheck(color);
            updateFaculty.setColor(color);
            return repository.save(updateFaculty);
        }

        if (name == null & color == null) {
            return updateFaculty;
        }

        service.validateCheck(name, color);
        updateFaculty.setName(name);
        updateFaculty.setColor(color);
        return repository.save(updateFaculty);
    }

    @Override
    public Faculty remove(Long id) {
        Faculty deleteFaculty = get(id);
        repository.delete(deleteFaculty);
        return deleteFaculty;
    }

    @Override
    public Collection<Faculty> getAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Faculty> getByNameOrColor(String name, String color) {
        if (name != null & color == null) {
            service.validateCheck(name);
            return repository.findByNameContainsIgnoreCase(name);
        }

        if (name == null & color != null) {
            service.validateCheck(color);
            return repository.findByColorContainsIgnoreCase(color);
        }

        if (name != null & color != null) {
            service.validateCheck(name, color);
            return repository.findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(name, color);
        }

        return repository.findAll();
    }

    @Override
    public Collection<Student> getStudentsByFacultyId(Long id) {
        return get(id).getStudents();
    }
}