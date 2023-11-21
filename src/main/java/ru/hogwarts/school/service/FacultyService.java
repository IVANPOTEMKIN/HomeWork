package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty add(Faculty faculty);

    Faculty get(long id);

    Faculty edit(long id, Faculty faculty);

    void remove(long id);

    Collection<Faculty> getAll();

    Collection<Faculty> getByColor(String color);
}