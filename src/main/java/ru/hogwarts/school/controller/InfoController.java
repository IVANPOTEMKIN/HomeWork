package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.AdditionalService;

@RestController
public class InfoController {

    @Autowired
    private AdditionalService service;

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/port")
    public String getPort() {
        return "Используемый порт: " + port;
    }

    @GetMapping("/number")
    public Integer getIntegerNumber() {
        return service.getIntegerNumber();
    }
}