package ru.hogwarts.school.model;

import ru.hogwarts.school.exception.InvalideInputException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    public Faculty() {
    }

    public Faculty(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color != null
                && !color.isBlank()
                && color.matches("[а-яА-Я -]+")) {
            this.color = color;
            return;
        }
        throw new InvalideInputException();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Faculty faculty = (Faculty) object;
        return Objects.equals(id, faculty.id)
                && Objects.equals(name, faculty.name)
                && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}