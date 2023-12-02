package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.hogwarts.school.exception.InvalideInputException;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @OneToMany(mappedBy = "faculty")
    @JsonIgnore
    private Collection<Student> students;

    public Faculty() {
    }

    public Faculty(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id != null && id > 0) {
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
                && name.matches("[а-яА-Я -]+")
                && !name.equalsIgnoreCase(getColor())) {
            this.name = name;
            return;
        }
        throw new InvalideInputException();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color != null
                && !color.isBlank()
                && color.matches("[а-яА-Я -]+")
                && !color.equalsIgnoreCase(getName())) {
            this.color = color;
            return;
        }
        throw new InvalideInputException();
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Faculty faculty = (Faculty) object;
        return Objects.equals(id, faculty.id)
                && Objects.equals(name, faculty.name)
                && Objects.equals(color, faculty.color)
                && Objects.equals(students, faculty.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, students);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", students=" + students +
                '}';
    }
}