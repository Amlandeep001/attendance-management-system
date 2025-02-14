package com.org.attendancemicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.org.attendancemicroservice.constant.AttendanceStatus;
import com.org.attendancemicroservice.model.Attendance;

@ExtendWith(MockitoExtension.class)
public class AttendanceCalculatorServiceTest
{

	@Mock
	private AttendanceService attendanceService; // Mocking AttendanceService

	@InjectMocks
	private AttendanceCalculatorService attendanceCalculatorService; // Injecting Mock into class under test

	@BeforeEach
	void setUp()
	{
		// No need for explicit setup due to @InjectMocks
	}

	@Test
	void testCalculateAndPersistAttendance_Present()
	{
		// Given: Valid input message
		String message = "Employee 123 worked 8.0 hours on 2024-02-13";

		// Mock saveAttendance (void method, so doNothing is optional)
		doNothing().when(attendanceService).saveAttendance(any(Attendance.class));

		// When: Calling the method under test
		attendanceCalculatorService.calculateAndPersistAttendance(message);

		// Then: Verify saveAttendance() was called with expected Attendance object
		ArgumentCaptor<Attendance> attendanceCaptor = ArgumentCaptor.forClass(Attendance.class);
		verify(attendanceService, times(1)).saveAttendance(attendanceCaptor.capture());

		Attendance capturedAttendance = attendanceCaptor.getValue();

		assertNotNull(capturedAttendance);
		assertEquals("123", capturedAttendance.getEmployeeId());
		assertEquals(LocalDate.of(2024, 2, 13), capturedAttendance.getDate());
		assertEquals(8.0, capturedAttendance.getTotalHours());
		assertEquals(AttendanceStatus.PRESENT, capturedAttendance.getStatus());
		assertEquals("123@outlook.com", capturedAttendance.getEmailId());
	}

	@Test
	void testCalculateAndPersistAttendance_HalfDay()
	{
		// Given: Employee worked for 5.0 hours (should be HALF_DAY)
		String message = "Employee 456 worked 5.0 hours on 2024-02-14";

		doNothing().when(attendanceService).saveAttendance(any(Attendance.class));

		attendanceCalculatorService.calculateAndPersistAttendance(message);

		ArgumentCaptor<Attendance> attendanceCaptor = ArgumentCaptor.forClass(Attendance.class);
		verify(attendanceService, times(1)).saveAttendance(attendanceCaptor.capture());

		Attendance capturedAttendance = attendanceCaptor.getValue();

		assertNotNull(capturedAttendance);
		assertEquals("456", capturedAttendance.getEmployeeId());
		assertEquals(LocalDate.of(2024, 2, 14), capturedAttendance.getDate());
		assertEquals(5.0, capturedAttendance.getTotalHours());
		assertEquals(AttendanceStatus.HALF_DAY, capturedAttendance.getStatus());
	}

	@Test
	void testCalculateAndPersistAttendance_Absent()
	{
		// Given: Employee worked for 2.0 hours (should be ABSENT)
		String message = "Employee 789 worked 2.0 hours on 2024-02-15";

		doNothing().when(attendanceService).saveAttendance(any(Attendance.class));

		attendanceCalculatorService.calculateAndPersistAttendance(message);

		ArgumentCaptor<Attendance> attendanceCaptor = ArgumentCaptor.forClass(Attendance.class);
		verify(attendanceService, times(1)).saveAttendance(attendanceCaptor.capture());

		Attendance capturedAttendance = attendanceCaptor.getValue();

		assertNotNull(capturedAttendance);
		assertEquals("789", capturedAttendance.getEmployeeId());
		assertEquals(LocalDate.of(2024, 2, 15), capturedAttendance.getDate());
		assertEquals(2.0, capturedAttendance.getTotalHours());
		assertEquals(AttendanceStatus.ABSENT, capturedAttendance.getStatus());
	}

	@Test
	void testCalculateAndPersistAttendance_InvalidMessageFormat()
	{
		// Given: An invalid message format
		String invalidMessage = "Invalid message format";

		// When & Then: Expect an exception
		assertThrows(ArrayIndexOutOfBoundsException.class, () ->
		{
			attendanceCalculatorService.calculateAndPersistAttendance(invalidMessage);
		});

		// Verify that saveAttendance was never called
		verify(attendanceService, never()).saveAttendance(any(Attendance.class));
	}

	@Test
	void testCalculateAndPersistAttendance_SaveThrowsException()
	{
		// Given: A valid message
		String message = "Employee 123 worked 8.0 hours on 2024-02-13";

		// Mock exception in saveAttendance
		doThrow(new RuntimeException("Database error")).when(attendanceService).saveAttendance(any(Attendance.class));

		// When & Then: Expect exception
		RuntimeException thrownException = assertThrows(RuntimeException.class, () ->
		{
			attendanceCalculatorService.calculateAndPersistAttendance(message);
		});

		assertEquals("Database error", thrownException.getMessage());

		// Verify that saveAttendance was still called once before throwing the exception
		verify(attendanceService, times(1)).saveAttendance(any(Attendance.class));
	}
}
