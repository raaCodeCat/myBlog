package ru.rakhmanov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rakhmanov.repository.PostRepository;
import ru.rakhmanov.service.FileStorageService;
import ru.rakhmanov.service.ImageGarbageCollectorService;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageGarbageCollectorServiceImpl implements ImageGarbageCollectorService {

    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    @Override
    public void cleanupImages() {
        AtomicInteger deletedImagesCount = new AtomicInteger(0);

        List<String> listImageUrlsInDb = getPostImages();
        List<String> listImagesInFolder = getAllImageNames();

        listImagesInFolder.stream()
                .filter(imageName -> !listImageUrlsInDb.contains(imageName))
                .forEach(path -> {
                    deletedImagesCount.getAndIncrement();
                    fileStorageService.deleteFile(path);
                });

        log.info("Deleted {} images from folder", deletedImagesCount.get());
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
