package ru.rakhmanov.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rakhmanov.service.ImageGarbageCollectorService;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.image-garbage-collector.enabled", havingValue = "true")
public class ImageGarbageCollectorScheduler extends BaseScheduler {

    private final String SCHEDULER_NAME = "ImageGarbageCollectorScheduler";

    private final ImageGarbageCollectorService imageGarbageCollectorService;

    @Override
    @Scheduled(cron = "${job.image-garbage-collector.cron}")
    protected void execute() {
        super.execute();
    }

    @Override
    protected String getSchedulerName() {
        return SCHEDULER_NAME;
    }

    @Override
    protected void process() {
        imageGarbageCollectorService.cleanupImages();
    }
}
