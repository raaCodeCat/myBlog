package ru.rakhmanov.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveImage(MultipartFile image);

}
