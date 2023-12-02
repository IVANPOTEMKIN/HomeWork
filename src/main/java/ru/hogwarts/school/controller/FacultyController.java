package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %S. Error: %S", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping
    public Faculty add(@RequestParam String name,
                       @RequestParam String color) {
        return service.add(name, color);
    }

    @GetMapping("{id}")
    public Faculty get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all")
    public Collection<Faculty> getAll() {
        return service.getAll();
    }

    @GetMapping
    public Collection<Faculty> getByNameOrColor(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String color) {
        return service.getByNameOrColor(name, color);
    }

    @GetMapping(params = "id")
    public Collection<Student> getStudents(@RequestParam(required = false) Long id) {
        return service.getStudentsByFacultyId(id);
    }

    @PutMapping("{id}")
    public Faculty edit(@PathVariable Long id,
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String color) {
        return service.edit(id, name, color);
    }

    @DeleteMapping("{id}")
    public Faculty remove(@PathVariable Long id) {
        return service.remove(id);
    }
}