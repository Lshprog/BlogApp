package com.example.TravelPlanner.travelplanning.common;

import com.example.TravelPlanner.common.config.ApplicationContextProvider;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

public class VotingResultsJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long votingId = context.getJobDetail().getJobDataMap().getLong("votingId");

        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        CheckService checkService = applicationContext.getBean(CheckService.class);
        checkService.checkVotingResults(votingId);

    }
}
