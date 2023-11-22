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
    public Faculty add(Faculty faculty) {
        service.validateCheck(faculty);
        service.isFacultyAlreadyAdded(getAll(), faculty);
        return repository.save(faculty);
    }

    @Override
    public Faculty get(Long id) {
        service.validateCheck(id);
        return repository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public Faculty edit(Long id, Faculty faculty) {
        Faculty updateFaculty = get(id);

        service.validateCheck(faculty);
        updateFaculty.setName(faculty.getName());
        updateFaculty.setColor(faculty.getColor());

        return repository.save(updateFaculty);
    }

    @Override
    public void remove(Long id) {
        Faculty deleteFaculty = get(id);
        repository.deleteById(deleteFaculty.getId());
    }

    @Override
    public Collection<Faculty> getAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Faculty> getByNameOrColor(String name, String color) {
        if (name != null & color == null) {
            service.validateCheck(name);
            return repository.findByNameIgnoreCase(name);
        }
        if (name == null & color != null) {
            service.validateCheck(color);
            return repository.findByColorIgnoreCase(color);
        }
        if (name != null & color != null) {
            service.validateCheck(name, color);
            return repository.findByNameIgnoreCaseAndColorIgnoreCase(name, color);
        }
        return repository.findAll();
    }

    @Override
    public Collection<Student> getStudentsByFacultyId(Long id) {
        return get(id).getStudents();
    }
}