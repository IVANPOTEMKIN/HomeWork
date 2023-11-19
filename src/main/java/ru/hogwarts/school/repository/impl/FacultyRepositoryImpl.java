package ru.hogwarts.school.repository.impl;

import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.CheckService;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository("facultyRep")
public class FacultyRepositoryImpl implements FacultyRepository {

    private final Map<Long, Faculty> faculties;
    private static long counterId;
    private final CheckService service;

    public FacultyRepositoryImpl(CheckService service) {
        faculties = new HashMap<>();
        counterId = 0;
        this.service = service;
    }

    @Override
    @PostConstruct
    public void init() {
        add(new Faculty(0, "Гриффиндор", "красный"));
        add(new Faculty(0, "Слизерин", "зеленый"));
        add(new Faculty(0, "Когтевран", "синий"));
        add(new Faculty(0, "Пуффендуй", "желтый"));
    }

    @Override
    public Faculty add(Faculty faculty) {
        service.validateCheck(faculty);
        service.isFacultyAlreadyAdded(faculties, faculty);

        Faculty newFaculty = new Faculty(++counterId, faculty.getName(), faculty.getColor());
        faculties.put(counterId, newFaculty);
        return newFaculty;
    }

    @Override
    public Faculty get(long id) {
        service.isNotFacultyContains(faculties, id);

        return faculties.get(id);
    }

    @Override
    public Faculty edit(long id, Faculty faculty) {
        Faculty updateFaculty = get(id);

        service.validateCheck(faculty);
        updateFaculty.setName(faculty.getName());
        updateFaculty.setColor(faculty.getColor());

        faculties.put(id, updateFaculty);
        return faculty;
    }

    @Override
    public Faculty remove(long id) {
        service.isNotFacultyContains(faculties, id);

        return faculties.remove(id);
    }

    @Override
    public Collection<Faculty> getAll() {
        return Collections.unmodifiableCollection(faculties.values());
    }

    @Override
    public Collection<Faculty> getByColor(String color) {
        return faculties.values()
                .stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toUnmodifiableList());
    }
}