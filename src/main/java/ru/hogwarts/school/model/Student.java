package ru.hogwarts.school.model;

import ru.hogwarts.school.exception.InvalideInputException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;

    public Student() {
    }

    public Student(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        if (age != null && age > 0) {
            this.age = age;
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
                && Objects.equals(age, student.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
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