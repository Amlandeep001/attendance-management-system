package com.org.eventstore.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.org.eventstore.model.SwipeEvent;
import com.org.eventstore.service.SwipeEventService;

@RestController
@RequestMapping("/swipe")
public class SwipeEventController
{
	private final SwipeEventService swipeEventService;

	public SwipeEventController(SwipeEventService swipeEventService)
	{
		this.swipeEventService = swipeEventService;
	}

	@PostMapping("/in")
	public void handleSwipeInEvent(@RequestBody SwipeEvent swipeEvent)
	{
		swipeEventService.handleSwipeInEvent(swipeEvent);
	}

	@PostMapping("/out")
	public void handleSwipeOutEvent(@RequestBody SwipeEvent swipeEvent)
	{
		swipeEventService.handleSwipeOutEvent(swipeEvent);
	}
}
