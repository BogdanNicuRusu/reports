package com.nicu.reports.config;

import static com.nicu.reports.scheduler.ReportProcessorJob.REPORT_PROCESSOR_JOB_DESCRIPTION;
import static com.nicu.reports.scheduler.ReportProcessorJob.REPORT_PROCESSOR_JOB_NAME;
import static com.nicu.reports.scheduler.ReportProcessorJob.REPORT_PROCESSOR_JOB_TRIGGER;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nicu.reports.scheduler.ReportProcessorJob;

@Configuration
public class QuartzConfig {

    @Value("${scheduler.report_processor.cron_expression:0 * * * * ?}")
    private String reportJobCronExpressions;

    @Bean
    public JobDetail reportJob() {
        return JobBuilder.newJob(ReportProcessorJob.class).storeDurably()
            .withIdentity(REPORT_PROCESSOR_JOB_NAME).withDescription(REPORT_PROCESSOR_JOB_DESCRIPTION).build();
    }

    @Bean
    public Trigger reportTrigger() {
        return newTrigger().withIdentity(REPORT_PROCESSOR_JOB_TRIGGER).forJob(reportJob()).
            withSchedule(CronScheduleBuilder.cronSchedule(reportJobCronExpressions)).build();
    }
}
