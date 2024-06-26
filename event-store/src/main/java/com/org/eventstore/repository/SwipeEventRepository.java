package com.org.eventstore.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.org.eventstore.model.SwipeEvent;

public interface SwipeEventRepository extends MongoRepository<SwipeEvent, String>
{
	@Aggregation(pipeline = {"{ '$group': { '_id' : '$employeeId' } }"})
	Set<String> findDistinctEmployeeIds();

	List<SwipeEvent> findByEmployeeIdAndTimestampBetween(String employeeId, Date startDate, Date endDate);
}
