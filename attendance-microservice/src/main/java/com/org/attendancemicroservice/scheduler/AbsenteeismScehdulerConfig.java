package com.org.attendancemicroservice.scheduler;

import java.time.LocalDate;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.org.attendancemicroservice.service.AbsenteeismNotificationService;

@Configuration
@EnableScheduling
public class AbsenteeismScehdulerConfig
{
	private final AbsenteeismNotificationService absenteeismNotificationService;

	public AbsenteeismScehdulerConfig(AbsenteeismNotificationService absenteeismNotificationService)
	{
		this.absenteeismNotificationService = absenteeismNotificationService;
	}

	// @Scheduled(cron = "0 0 8 * * MON-FRI") // Run every weekday at 8:00 AM
	// @Scheduled(cron = "0 */2 * ? * *")
	@Scheduled(cron = "0 0/2 * * * ?")
	public void checkAbsenteeismAndNotify()
	{
		LocalDate presentDay = LocalDate.now();
		LocalDate lastDay = presentDay.minusDays(1);

		absenteeismNotificationService.checkAbsenteeismAndNotify(lastDay);
	}
}
