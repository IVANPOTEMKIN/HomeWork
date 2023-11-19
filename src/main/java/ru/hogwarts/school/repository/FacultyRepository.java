package ru.hogwarts.school.repository;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyRepository {
    void init();

    Faculty add(Faculty faculty);

    Faculty get(long id);

    Faculty edit(long id, Faculty faculty);

    Faculty remove(long id);

    Collection<Faculty> getAll();

    Collection<Faculty> getByColor(String color);
}