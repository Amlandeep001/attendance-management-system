package com.org.eventstore.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.org.eventstore.model.SwipeEvent;
import com.org.eventstore.repository.SwipeEventRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SwipeEventService
{
	private final SwipeEventRepository swipeEventRepository;
	private final TotalHoursCalculatorService totalHoursCalculatorService;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final String attendanceTopic;

	public SwipeEventService(SwipeEventRepository swipeEventRepository, TotalHoursCalculatorService totalHoursCalculatorService,
			KafkaTemplate<String, String> kafkaTemplate, @Value("${attendance.topic}") String attendanceTopic)
	{
		this.swipeEventRepository = swipeEventRepository;
		this.totalHoursCalculatorService = totalHoursCalculatorService;
		this.kafkaTemplate = kafkaTemplate;
		this.attendanceTopic = attendanceTopic;
	}

	public void handleSwipeInEvent(SwipeEvent swipeEvent)
	{
		swipeEvent.setId(gerenateRandomId());
		swipeEvent.setSwipeType("in");
		saveSwipeEvent(swipeEvent);
	}

	public void handleSwipeOutEvent(SwipeEvent swipeEvent)
	{
		swipeEvent.setId(gerenateRandomId());
		swipeEvent.setSwipeType("out");
		saveSwipeEvent(swipeEvent);
	}

	private String gerenateRandomId()
	{
		return UUID.randomUUID().toString().split("-")[0];
	}

	private void saveSwipeEvent(SwipeEvent swipeEvent)
	{
		swipeEventRepository.save(swipeEvent);
	}

	public void calculateTotalHoursAndPublishToKafka(LocalDate date)
	{
		Set<String> employeeIds = swipeEventRepository.findDistinctEmployeeIds();
		for(String employeeId : employeeIds)
		{
			// Calculate total hours for each employee
			double totalHours = totalHoursCalculatorService.calculateTotalHoursForEmployee(employeeId, date);

			String attendanceInfo = String.format("Employee %s worked %.2f hours, for the date: %s", employeeId, totalHours, date);

			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(attendanceTopic, attendanceInfo);
			future.whenComplete((result, ex) ->
			{
				if(ex == null)
				{
					log.info("Sent attendanceInfo=[" + attendanceInfo +
							"] with offset=[" + result.getRecordMetadata().offset() + "]");
				}
				else
				{
					log.error("Unable to send attendanceInfo=[" +
							attendanceInfo + "] due to : " + ex.getMessage());
				}
			});
		}
	}
}
