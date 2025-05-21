package ru.rakhmanov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rakhmanov.repository.PostRepository;
import ru.rakhmanov.service.FileStorageService;
import ru.rakhmanov.service.ImageGarbageCollectorService;

import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageGarbageCollectorServiceImpl implements ImageGarbageCollectorService {

    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    @Override
    public void cleanupImages() {
        List<String> listImageUrlsInDb = getPostImages();
        List<String> listImagesInFolder = getAllImageNames();

        listImagesInFolder.stream()
                .filter(imageName -> !listImageUrlsInDb.contains(imageName))
                .forEach(fileStorageService::deleteFile);
    }

    private List<String> getPostImages() {
        List<String> imageUrls = postRepository.getPostImageUrls();

        return imageUrls.stream()
                .map(path -> Paths.get(path).getFileName().toString())
                .toList();
    }

    private List<String> getAllImageNames() {
        return fileStorageService.getAllFiles();
    }
}
