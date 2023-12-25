package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.CheckService;
import ru.hogwarts.school.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository repository;
    private final StudentService studentService;
    private final CheckService checkService;
    private final String avatarsDir;
    private final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    public AvatarServiceImpl(AvatarRepository repository,
                             StudentService studentService,
                             CheckService checkService,
                             @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.repository = repository;
        this.studentService = studentService;
        this.checkService = checkService;
        this.avatarsDir = avatarsDir;
    }

    @Override
    public ResponseEntity<String> uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Вызван метод \"uploadAvatar()\" сервиса \"Avatar\"");

        Student student = studentService.get(studentId);
        Path filePath = Path.of(avatarsDir, student.getName() + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        logger.info("Аватар УСПЕШНО СОХРАНЕН");
        repository.save(avatar);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<byte[]> downloadAvatarFromDB(Long id) {
        logger.info("Вызван метод \"downloadAvatarFromDB()\" сервиса \"Avatar\"");

        Avatar avatar = get(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        logger.info("Аватар УСПЕШНО ПОЛУЧЕН");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @Override
    public void downloadAvatarFromFile(Long id, HttpServletResponse response) throws IOException {
        logger.info("Вызван метод \"downloadAvatarFromFile()\" сервиса \"Avatar\"");

        Avatar avatar = get(id);
        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);

            logger.info("Аватар УСПЕШНО ПОЛУЧЕН");
        }
    }

    @Override
    public Avatar get(Long id) {
        logger.info("Вызван метод \"get({})\" сервиса \"Avatar\"", id);

        checkService.validateCheck(id);
        return repository.findById(id).orElseThrow(AvatarNotFoundException::new);
    }

    @Override
    public Avatar findAvatar(Long studentId) {
        logger.info("Вызван метод \"findAvatar({})\" сервиса \"Avatar\"", studentId);

        checkService.validateCheck(studentId);
        return repository.findByStudentId(studentId).orElseGet(Avatar::new);
    }

    private String getExtensions(String fileName) {
        logger.info("Вызван метод \"getExtensions()\" сервиса \"Avatar\"");

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public Collection<Avatar> getAll(Integer pageNumber, Integer pageSize) {
        logger.info("Вызван метод \"getAll({}, {})\" сервиса \"Avatar\"", pageNumber, pageSize);

        checkService.validateCheck(pageNumber);
        checkService.validateCheck(pageSize);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);

        logger.info("Список всех автаров УСПЕШНО ПОЛУЧЕН");
        return repository.findAll(pageRequest).getContent();
    }
}