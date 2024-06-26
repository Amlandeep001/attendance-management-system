package com.org.attendancemicroservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumerService
{
	private final AttendanceCalculatorService attendanceCalculatorService;

	public KafkaConsumerService(AttendanceCalculatorService attendanceCalculatorService)
	{
		this.attendanceCalculatorService = attendanceCalculatorService;
	}

	@KafkaListener(topics = "${attendance.topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void consume(String message)
	{
		log.info(String.format("$$$$ => Consumed message: %s", message));

		// Parse Kafka message and call attendance calculator service
		attendanceCalculatorService.calculateAndPersistAttendance(message);
	}
}
