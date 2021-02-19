package com.STO.MHP.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.STO.MHP.models.Event;
import com.STO.MHP.repository.EventRepository;
import com.STO.MHP.services.IEventService;




@Service
public class EventService implements IEventService {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public Event save(Event entity) {
		return eventRepository.save(entity);
	}

	@Override
	public Event update(Event entity) {
		return eventRepository.save(entity);
	}

	@Override
	public void delete(Event entity) {
		eventRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		eventRepository.deleteById(id);
	}

	@Override
	public Event find(Long id) {
		return eventRepository.findById(id).orElse(null);
	}

	@Override
	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Event> event) {
		eventRepository.deleteInBatch(event);
	}


	
}
