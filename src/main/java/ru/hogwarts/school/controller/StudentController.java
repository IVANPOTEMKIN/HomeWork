package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %S. Error: %S", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping
    public Student add(@RequestParam String name,
                       @RequestParam Integer age,
                       @RequestParam Long facultyId) {
        return service.add(name, age, facultyId);
    }

    @GetMapping("{id}")
    public Student get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all")
    public Collection<Student> getAll() {
        return service.getAll();
    }

    @GetMapping("/last-five-students")
    public Collection<Student> getLastFiveStudents() {
        return service.getLastFiveStudents();
    }

    @GetMapping("/amount-all-students")
    public Integer getAmountAllStudents() {
        return service.getAmountAllStudents();
    }

    @GetMapping
    public Collection<Student> getByAge(@RequestParam(required = false) Integer minAge,
                                        @RequestParam(required = false) Integer maxAge) {
        return service.getByAge(minAge, maxAge);
    }

    @GetMapping(params = "name")
    public Collection<Student> getByAge(@RequestParam(required = false) String name) {
        return service.getByName(name);
    }

    @GetMapping("/average-age-students")
    public String getAvgAgeStudents() {
        return service.getAvgAgeStudents();
    }

    @GetMapping("/average-age-all-students")
    public Double getAvgAgeAllStudents() {
        return service.getAvgAgeAllStudents();
    }

    @GetMapping("/sorted-by-name")
    public Collection<String> getBySortedName(@RequestParam(required = false) String prefix) {
        return service.getBySortedName(prefix);
    }

    @GetMapping("/print-parallel")
    public void getNames() {
        service.getNames();
    }

    @GetMapping("/print-synchronized")
    public void getNamesWithSynchronizedThread() {
        service.getNamesWithSynchronizedThread();
    }

    @GetMapping(params = "id")
    public Faculty getFaculty(@RequestParam(required = false) Long id) {
        return service.getFacultyById(id);
    }

    @PutMapping("{id}")
    public Student edit(@PathVariable Long id,
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) Integer age,
                        @RequestParam(required = false) Long facultyId) {
        return service.edit(id, name, age, facultyId);
    }

    @DeleteMapping("{id}")
    public Student remove(@PathVariable Long id) {
        return service.remove(id);
    }
}