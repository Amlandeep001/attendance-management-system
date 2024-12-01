package com.org.attendancemicroservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.org.attendancemicroservice.model.Attendance;
import com.org.attendancemicroservice.repository.AttendanceRepository;

@Service
public class AbsenteeismNotificationService
{
	private final AttendanceRepository attendanceRepository;
	private final NotificationService notificationService;

	public AbsenteeismNotificationService(AttendanceRepository attendanceRepository, NotificationService notificationService)
	{
		this.attendanceRepository = attendanceRepository;
		this.notificationService = notificationService;
	}

	public void checkAbsenteeismAndNotify(LocalDate date)
	{
		// Logic to check absenteeism based on attendance data
		List<Attendance> absentEmployees = attendanceRepository.findAbsentEmployeesByDate(date);
		for(Attendance attendance : absentEmployees)
		{
			final String employeeId = attendance.getEmployeeId();
			final String recipientEmail = attendance.getEmailId();
			final String subject = "Absenteeism Notification";
			final String content = "You have been marked as absent. Please contact your manager for further information.";
			// Send notification to absentee employee using notification service
			notificationService.sendNotification(employeeId, date, recipientEmail, subject, content);
		}
	}
}
