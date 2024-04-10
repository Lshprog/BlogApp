package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.common.VotingResultsJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class DynamicSchedulingService {
    private final Scheduler scheduler;

    @Autowired
    public DynamicSchedulingService(Scheduler scheduler) {
        this.scheduler = scheduler;
        startScheduler();
    }

    private void startScheduler() {
        try {
            this.scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void scheduleVotingEventJob(Long votingId, LocalDateTime endTime) throws SchedulerException {
        log.info("in scheduler");
        ZoneId singaporeZoneId = ZoneId.of("Asia/Singapore");
        Date startDate = Date.from(endTime.atZone(singaporeZoneId).toInstant());

        if (!startDate.after(new Date())) {
            throw new SchedulerException();
        }

        JobDetail jobDetail = JobBuilder.newJob(VotingResultsJob.class)
                .withIdentity("votingEventJob-" + votingId, "jobs")
                .usingJobData("votingId", votingId)
                .build();

        log.info("End time: " + startDate);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("votingEventTrigger-" + votingId, "triggers")
                .startAt(startDate)
                .build();

        log.info("after trigger");

        scheduler.scheduleJob(jobDetail, trigger);
    }
}

