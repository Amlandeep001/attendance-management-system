package com.org.attendancemicroservice.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.attendancemicroservice.model.Attendance;
import com.org.attendancemicroservice.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController
{
	private final AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService)
	{
		this.attendanceService = attendanceService;
	}

	@GetMapping("/{employeeId}/{date}")
	public ResponseEntity<String> getAttendance(@PathVariable String employeeId, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
	{
		Attendance attendance = attendanceService.getAttendance(employeeId, date);

		if(attendance != null)
		{
			return ResponseEntity.ok("Required attendance info: " + attendance);
		}
		else
		{
			return ResponseEntity.ok("No record found");
		}
	}

	@GetMapping("/total-hours/{employeeId}/{date}")
	public ResponseEntity<String> getTotalHours(@PathVariable String employeeId, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
	{
		Double totalHours = attendanceService.getTotalHours(employeeId, date);
		if(totalHours != null)
		{
			return ResponseEntity.ok("Total hours for employee: " + employeeId + " on : " + date + " is: " + totalHours);
		}
		else
		{
			return ResponseEntity.ok("No record found");
		}
	}
}
