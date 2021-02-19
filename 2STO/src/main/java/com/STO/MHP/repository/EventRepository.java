package com.STO.MHP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.STO.MHP.models.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	//User findByEmail(String email);
}
