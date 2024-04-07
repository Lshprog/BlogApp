package com.example.TravelPlanner.travelplanning.common;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class DynamicSchedulingService {

    @Autowired
    private Scheduler scheduler;

    public void scheduleVotingEventJob(Long votingId, LocalDateTime endTime) throws SchedulerException {
        Date startDate = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

        if (!startDate.after(new Date())) {
            throw new SchedulerException();
        }

        JobDetail jobDetail = JobBuilder.newJob(VotingResultsJob.class)
                .withIdentity("votingEventJob-" + votingId, "jobs")
                .usingJobData("votingId", votingId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("votingEventTrigger-" + votingId, "triggers")
                .startAt(startDate)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}

