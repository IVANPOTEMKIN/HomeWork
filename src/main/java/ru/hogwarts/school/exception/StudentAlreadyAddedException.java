package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentAlreadyAddedException extends HttpStatusCodeException {
    public StudentAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, "Студент уже добавлен!");
    }
}