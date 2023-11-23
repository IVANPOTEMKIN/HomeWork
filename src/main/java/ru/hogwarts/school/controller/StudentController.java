package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %S. Error: %S", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        return service.add(student);
    }

    @GetMapping("{id}")
    public Student get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all")
    public Collection<Student> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "age")
    public Collection<Student> getByAge(@RequestParam Integer age) {
        return service.getByAge(age);
    }

    @PutMapping("{id}")
    public Student edit(@PathVariable Long id, @RequestBody Student student) {
        return service.edit(id, student);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }
}