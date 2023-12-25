package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    @Autowired
    private AvatarService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %S. Error: %S", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId,
                                               @RequestParam MultipartFile avatar) throws IOException {
        return service.uploadAvatar(studentId, avatar);
    }

    @GetMapping("/all")
    public Collection<Avatar> getAll(@RequestParam Integer pageNumber,
                                     @RequestParam Integer pageSize) {
        return service.getAll(pageNumber, pageSize);
    }

    @GetMapping(value = "/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        return service.downloadAvatarFromDB(id);
    }

    @GetMapping(value = "/{id}/avatar-from-file")
    public void downloadAvatarFromFile(@PathVariable Long id,
                                       HttpServletResponse response) throws IOException {
        service.downloadAvatarFromFile(id, response);
    }
}