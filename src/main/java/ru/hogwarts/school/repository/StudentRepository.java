package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(Integer age);

    Collection<Student> findByAgeBetween(Integer min, Integer max);

    @Query(value = "SELECT COUNT(*) FROM student s", nativeQuery = true)
    Integer getAmountAllStudents();

    @Query(value = "SELECT AVG(age) FROM student s", nativeQuery = true)
    Double getAvgAgeStudents();

    @Query(value = "SELECT * FROM student s ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> getLastFiveStudents();
}