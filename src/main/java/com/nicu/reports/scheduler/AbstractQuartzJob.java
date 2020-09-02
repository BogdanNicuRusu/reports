package com.nicu.reports.scheduler;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractQuartzJob<T> extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String className = this.getClass().getSimpleName();
        log.info("Starting {}", className);
        long start = System.currentTimeMillis();
        List<T> items = readItems();
        log.info("{} will process {} items", className, items.size());
        items.forEach(this::processItem);
        long end = System.currentTimeMillis();
        log.info("{} took {} ms to process successfully {} items with {} failures", className, (end - start), successes(items), failures(items));
    }

    abstract List<T> readItems();

    abstract void processItem(T item);

    abstract long successes(List<T> entities);

    abstract long failures(List<T> entities);
}
