package com.example.TravelPlanner.travelplanning.common;

import com.example.TravelPlanner.common.config.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

@Slf4j
public class VotingResultsJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long votingId = context.getJobDetail().getJobDataMap().getLong("votingId");
        log.info("in job");
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        CheckService checkService = applicationContext.getBean(CheckService.class);
        checkService.checkVotingResults(votingId);
        log.info("end of job");

    }
}
