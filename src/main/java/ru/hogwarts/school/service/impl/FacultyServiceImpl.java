package ru.hogwarts.school.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@Service("facultyService")
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;

    public FacultyServiceImpl(@Qualifier("facultyRep") FacultyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Faculty add(Faculty faculty) {
        return repository.add(faculty);
    }

    @Override
    public Faculty get(long id) {
        return repository.get(id);
    }

    @Override
    public Faculty edit(long id, Faculty faculty) {
        return repository.edit(id, faculty);
    }

    @Override
    public Faculty remove(long id) {
        return repository.remove(id);
    }

    @Override
    public Collection<Faculty> getAll() {
        return repository.getAll();
    }

    @Override
    public Collection<Faculty> getByColor(String color) {
        return repository.getByColor(color);
    }
}