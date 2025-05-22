package ru.rakhmanov.job;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseScheduler {

    protected void execute() {
        String schedulerName = getSchedulerName();
        long startTime = System.currentTimeMillis();

        beforeProcess(schedulerName);
        process();
        afterProcess(schedulerName);

        long endTime = System.currentTimeMillis();
        log.info("Scheduler [{}] worked {}ms", schedulerName, endTime - startTime);
    }

    private void beforeProcess(String schedulerName) {
        log.info("Scheduler [{}] is started processing", schedulerName);
    }

    private void afterProcess(String schedulerName) {
        log.info("Scheduler [{}] is finished processing", schedulerName);
    }

    protected String getSchedulerName() {
        return this.getClass().getName();
    }

    protected abstract void process();
}
