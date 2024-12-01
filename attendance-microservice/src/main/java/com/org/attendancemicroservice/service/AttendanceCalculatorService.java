package com.org.attendancemicroservice.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.org.attendancemicroservice.constant.AttendanceStatus;
import com.org.attendancemicroservice.model.Attendance;

@Service
public class AttendanceCalculatorService
{
	private final AttendanceService attendanceService;

	public AttendanceCalculatorService(AttendanceService attendanceService)
	{
		this.attendanceService = attendanceService;
	}

	// Calculate total hours and update attendance records based on swipe events
	public void calculateAndPersistAttendance(String message)
	{
		// Split the message by comma to extract the necessary information
		String[] parts = message.split(" ");

		// Extract employeeId, totalHours, and date from the message
		final String employeeInfo = parts[1];
		final double totalHours = Double.parseDouble(parts[3]);
		final LocalDate date = LocalDate.parse(parts[parts.length - 1], DateTimeFormatter.ISO_DATE);

		// Use the extracted information to calculate attendance
		AttendanceStatus status = this.calculateAttendanceStatus(totalHours);

		// Build Attendance object
		Attendance attendance = Attendance.builder()
				.employeeId(employeeInfo)
				.date(date)
				.totalHours(totalHours)
				.status(status)
				.emailId(employeeInfo + "@outlook.com")
				.build();

		// Save attendance to database
		attendanceService.saveAttendance(attendance);
	}

	private AttendanceStatus calculateAttendanceStatus(double totalHours)
	{
		// Logic to calculate attendance status based on total hours
		if(totalHours < 4)
		{
			return AttendanceStatus.ABSENT;
		}
		else if(totalHours >= 4 && totalHours < 8)
		{
			return AttendanceStatus.HALF_DAY;
		}
		else
		{
			return AttendanceStatus.PRESENT;
		}
	}
}
