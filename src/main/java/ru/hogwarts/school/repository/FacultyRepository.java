package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Collection<Faculty> findByNameContainsIgnoreCase(String name);

    Collection<Faculty> findByColorContainsIgnoreCase(String color);

    Collection<Faculty> findByNameContainsIgnoreCaseAndColorContainsIgnoreCase(String name, String color);

    @Query(value = "SELECT COUNT(*) FROM faculty f ", nativeQuery = true)
    Integer getAmountAllFaculties();
}