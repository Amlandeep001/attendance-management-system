package com.org.attendancemicroservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.org.attendancemicroservice.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>
{
	Attendance findByEmployeeIdAndDate(String employeeId, LocalDate date);

	@Query(value = "SELECT a.totalHours FROM Attendance a WHERE a.employeeId = :employeeId AND a.date = :date", nativeQuery = false)
	Double getTotalHoursForEmployeeAndDate(@Param("employeeId") String employeeId, @Param("date") LocalDate date);

	@Query(value = "SELECT a FROM Attendance a WHERE a.status = 'ABSENT' AND a.date = :date", nativeQuery = false)
	List<Attendance> findAbsentEmployeesByDate(@Param("date") LocalDate date);
}
