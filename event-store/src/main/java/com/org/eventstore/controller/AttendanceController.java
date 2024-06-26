package com.org.eventstore.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.eventstore.service.SwipeEventService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController
{
	private final SwipeEventService swipeEventService;

	public AttendanceController(SwipeEventService swipeEventService)
	{
		this.swipeEventService = swipeEventService;
	}

	@GetMapping("/calculate/{date}")
	public ResponseEntity<String> calculateTotalHoursForDate(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
	{
		swipeEventService.calculateTotalHoursAndPublishToKafka(date);
		return ResponseEntity.ok("Attendance calculation triggered for date: " + date);
	}
}
