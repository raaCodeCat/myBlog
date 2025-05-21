package ru.rakhmanov.job;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseScheduler {

    protected void execute(String schedulerName) {
        beforeProcess(schedulerName);
        process();
        afterProcess(schedulerName);
    }

    protected void process() {

    }

    private void beforeProcess(String schedulerName) {
        log.info("Scheduler [{}] is started processing", schedulerName);
    }

    private void afterProcess(String schedulerName) {
        log.info("Scheduler [{}] is finished processing", schedulerName);
    }
}
