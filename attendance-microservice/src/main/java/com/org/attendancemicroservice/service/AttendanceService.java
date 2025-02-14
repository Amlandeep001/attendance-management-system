package com.org.attendancemicroservice.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.org.attendancemicroservice.model.Attendance;
import com.org.attendancemicroservice.repository.AttendanceRepository;

@Service
public class AttendanceService
{
	private final AttendanceRepository attendanceRepository;

	public AttendanceService(AttendanceRepository attendanceRepository)
	{
		this.attendanceRepository = attendanceRepository;
	}

	public void saveAttendance(Attendance attendance)
	{
		attendanceRepository.save(attendance);
	}

	public Attendance getAttendance(String employeeId, LocalDate date)
	{
		// Logic to fetch attendance for a specific employee on a particular date
		return attendanceRepository.findByEmployeeIdAndDate(employeeId, date);
	}

	public Double getTotalHours(String employeeId, LocalDate date)
	{
		// Logic to calculate total hours for a specific employee on a particular date
		return attendanceRepository.getTotalHoursForEmployeeAndDate(employeeId, date);
	}
}
