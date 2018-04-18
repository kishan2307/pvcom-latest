package com.pvcom.config;

import com.pvcom.services.AutoEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Schedule {
    private static final Logger logger = LoggerFactory.getLogger(Schedule.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private AutoEntryService autoEntryService;

    @Scheduled(cron = "0 */15 * ? * *")
    public void scheduleTaskWithCronExpression() {
        logger.debug("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        autoEntryService.executeAutoSchedule();
    }
}
