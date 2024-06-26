package com.org.attendancemicroservice.service;

import java.time.LocalDate;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService
{
	private final JavaMailSender emailSender;

	public NotificationService(JavaMailSender emailSender)
	{
		this.emailSender = emailSender;
	}

	public void sendNotification(String employeeId, LocalDate date, String recipientEmail, String subject, String content)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(recipientEmail);
		message.setSubject(subject);
		message.setText(content);
		// emailSender.send(message);

		// Logic to send notification (e.g., email, SMS) for absenteeism
		log.info("Notification sent for absentee employee: {} for the date: {}", employeeId, date);
	}
}
