package ru.rakhmanov.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {

    String saveImage(MultipartFile image);

    List<String> getAllFiles();

    void deleteFile(String fileName);

}
