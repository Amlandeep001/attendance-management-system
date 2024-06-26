package com.org.eventstore.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.org.eventstore.model.SwipeEvent;
import com.org.eventstore.repository.SwipeEventRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TotalHoursCalculatorService
{
	private final SwipeEventRepository swipeEventRepository;
	// private final ZoneId timezone;

	public TotalHoursCalculatorService(SwipeEventRepository swipeEventRepository)
	{
		this.swipeEventRepository = swipeEventRepository;
		// this.timezone = ZoneId.of("America/New_York");
	}

	// Calculate total hours for an employee at the end of the day
	public double calculateTotalHoursForEmployee(String employeeId, LocalDate date)
	{
		LocalDate startDate = date.atStartOfDay().toLocalDate();
		LocalDate endDate = startDate.plusDays(1);

		Date timestampStart = formatLocalDateToDate(startDate);
		Date timestampEnd = formatLocalDateToDate(endDate);

		List<SwipeEvent> swipeEvents = swipeEventRepository.findByEmployeeIdAndTimestampBetween(employeeId, timestampStart, timestampEnd);

		log.info("EmployeeId: " + employeeId + " Events: " + swipeEvents);

		Duration totalDuration = Duration.ZERO;
		SwipeEvent previousEvent = null;

		for(SwipeEvent event : swipeEvents)
		{
			if(previousEvent != null && previousEvent.getSwipeType().equals("in") && event.getSwipeType().equals("out"))
			{
				totalDuration = totalDuration.plus(Duration.between(previousEvent.getTimestamp(), event.getTimestamp()));
			}
			previousEvent = event;
		}

		return totalDuration.toHours();
	}

	private Date formatLocalDateToDate(LocalDate localDate)
	{
		Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.of("UTC")));
		return Date.from(instant);
	}
}
