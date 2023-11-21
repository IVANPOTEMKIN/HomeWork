package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
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
    public Faculty get(long id) {
        return repository.findById(id).orElseThrow(FacultyNotFoundException::new);
    }

    @Override
    public Faculty edit(long id, Faculty faculty) {
        Faculty updateFaculty = get(id);

        service.validateCheck(faculty);
        updateFaculty.setId(id);
        updateFaculty.setName(faculty.getName());
        updateFaculty.setColor(faculty.getColor());

        return repository.save(updateFaculty);
    }

    @Override
    public void remove(long id) {
        Faculty deleteFaculty = get(id);
        repository.deleteById(deleteFaculty.getId());
    }

    @Override
    public Collection<Faculty> getAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Faculty> getByColor(String color) {
        return repository.findAllByColor(color);
    }
}