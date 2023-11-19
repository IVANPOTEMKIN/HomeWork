package ru.hogwarts.school.model;

import ru.hogwarts.school.exception.FacultyInvalideInputException;

import java.util.Objects;

public class Faculty {
    private final long id;
    private String name;
    private String color;

    public Faculty(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.matches("[а-яА-Я]+")) {
            this.name = name;
            return;
        }
        throw new FacultyInvalideInputException();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (color.matches("[а-яА-Я]+")) {
            this.color = color;
            return;
        }
        throw new FacultyInvalideInputException();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Faculty faculty = (Faculty) object;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
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