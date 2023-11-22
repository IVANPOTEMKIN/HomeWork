package ru.hogwarts.school.model;

import ru.hogwarts.school.exception.InvalideInputException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long age;
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Student() {
    }

    public Student(Long id, String name, Long age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id != null && id >= 0) {
            this.id = id;
            return;
        }
        throw new InvalideInputException();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null
                && !name.isBlank()
                && name.matches("[а-яА-Я ]+")) {
            this.name = name;
            return;
        }
        throw new InvalideInputException();
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        if (age != null && age > 0) {
            this.age = age;
            return;
        }
        throw new InvalideInputException();
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        if (faculty != null
                && faculty.getName() != null
                && !faculty.getName().isBlank()
                && faculty.getName().matches("[а-яА-Я ]+")
                && faculty.getColor() != null
                && !faculty.getColor().isBlank()
                && faculty.getColor().matches("[а-яА-Я -]+")
                && !faculty.getName().equalsIgnoreCase(faculty.getColor())
                && faculty.getId() != null
                && faculty.getId() >= 0) {
            this.faculty = faculty;
            return;
        }
        throw new InvalideInputException();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Student student = (Student) object;
        return Objects.equals(id, student.id)
                && Objects.equals(name, student.name)
                && Objects.equals(age, student.age)
                && Objects.equals(faculty, student.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, faculty);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}