package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.service.AdditionalService;

import java.util.stream.Stream;

@Service
public class AdditionalServiceImpl implements AdditionalService {

    private final Logger logger = LoggerFactory.getLogger(CheckServiceImpl.class);

    @Override
    public Integer getIntegerNumber() {
        logger.info("Вызван метод \"getIntegerNumber()\" сервиса \"AdditionalService\"");

        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);
    }
}