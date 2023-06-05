package com.smu.smuenip.infrastructure.config.batch;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

    @Bean
    public JobDetail userScoreUpdateJobDetail() {
        return JobBuilder.newJob(UserScoreUpdateJob.class)
                .withIdentity("userScoreUpdateJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger userScoreUpdateJobTrigger(JobDetail userScoreUpdateJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(userScoreUpdateJobDetail)
                .withIdentity("userScoreUpdateTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                .build();
    }
}
