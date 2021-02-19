package com.STO.MHP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.STO.MHP.models.Client;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	//User findByEmail(String email);
}
