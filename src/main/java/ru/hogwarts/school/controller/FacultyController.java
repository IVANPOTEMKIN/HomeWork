package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import ru.hogwarts.school.model.Faculty;
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
    public Faculty add(@RequestBody Faculty faculty) {
        return service.add(faculty);
    }

    @GetMapping("{id}")
    public Faculty get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all")
    public Collection<Faculty> getAll() {
        return service.getAll();
    }

    @GetMapping(params = "color")
    public Collection<Faculty> getByColor(@RequestParam String color) {
        return service.getByColor(color);
    }

    @PutMapping("{id}")
    public Faculty edit(@PathVariable Long id, @RequestBody Faculty faculty) {
        return service.edit(id, faculty);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable Long id) {
        service.remove(id);
    }
}