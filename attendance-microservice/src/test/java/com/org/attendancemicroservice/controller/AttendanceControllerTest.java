package com.org.attendancemicroservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.org.attendancemicroservice.constant.AttendanceStatus;
import com.org.attendancemicroservice.model.Attendance;
import com.org.attendancemicroservice.service.AttendanceService;

import lombok.val;

@SpringBootTest
public class AttendanceControllerTest
{
	@InjectMocks
	private AttendanceController attendanceController;

	@Mock
	private AttendanceService attendanceService;

	@Test
	public void getAttendanceTest()
	{
		final String employeeId = "12346";
		final LocalDate date = LocalDate.of(2024, 03, 04);

		val attendance = createAttendanceStub(employeeId, date);
		final String stubbedResponse = "Required attendance info: " + attendance;

		when(attendanceService.getAttendance(employeeId, date)).thenReturn(attendance);
		ResponseEntity<String> responseEntity = attendanceController.getAttendance(employeeId, date);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(stubbedResponse, responseEntity.getBody());

		verify(attendanceService).getAttendance(employeeId, date);
	}

	@Test
	public void getTotalHoursTest()
	{
		final String employeeId = "12346";
		final LocalDate date = LocalDate.of(2024, 03, 04);
		final Double totalHours = 3.00;

		final String stubbedResponse = "Total hours for employee: " + employeeId + " on : " + date + " is: " + totalHours;

		when(attendanceService.getTotalHours(employeeId, date)).thenReturn(totalHours);
		ResponseEntity<String> responseEntity = attendanceController.getTotalHours(employeeId, date);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(stubbedResponse, responseEntity.getBody());

		verify(attendanceService).getTotalHours(employeeId, date);
	}

	private Attendance createAttendanceStub(String empId, LocalDate date)
	{
		return Attendance.builder()
				.employeeId(empId)
				.date(date)
				.totalHours(3.00)
				.status(AttendanceStatus.ABSENT)
				.emailId(empId + "@outlook.com")
				.build();
	}
}
