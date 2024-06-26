package com.org.attendancemicroservice.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.org.attendancemicroservice.constant.AttendanceStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String employeeId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate date;

	double totalHours;

	@Enumerated(EnumType.STRING)
	AttendanceStatus status;

	String emailId;
}
