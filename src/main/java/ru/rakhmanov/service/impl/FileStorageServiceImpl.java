package ru.rakhmanov.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.rakhmanov.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String LINK_PATH = "/uploads/";
    private static final String UPLOAD_DIR = System.getProperty("catalina.base") + "/webapps" + LINK_PATH;

    @Override
    public String saveImage(MultipartFile image) {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.write(filePath, image.getBytes());

            return LINK_PATH + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
