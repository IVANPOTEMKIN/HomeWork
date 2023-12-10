package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public interface AvatarService {
    ResponseEntity<String> uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    ResponseEntity<byte[]> downloadAvatarFromDB(Long id);

    void downloadAvatarFromFile(Long id, HttpServletResponse response) throws IOException;

    Avatar get(Long id);

    Avatar findAvatar(Long studentId);

    Collection<Avatar> getAll(Integer pageNumber, Integer pageSize);
}