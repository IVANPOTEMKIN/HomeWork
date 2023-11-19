package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends HttpStatusCodeException {
    public StudentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Студент не найден!");
    }
}