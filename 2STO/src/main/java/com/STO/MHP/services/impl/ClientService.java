package com.STO.MHP.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.STO.MHP.models.Client;
import com.STO.MHP.repository.ClientRepository;
import com.STO.MHP.services.IClientService;



@Service
public class ClientService implements IClientService {
	
	@Autowired
	private ClientRepository userRepository;
	
	@Override
	public Client save(Client entity) {
		return userRepository.save(entity);
	}

	@Override
	public Client update(Client entity) {
		return userRepository.save(entity);
	}

	@Override
	public void delete(Client entity) {
		userRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public Client find(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public List<Client> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Client> users) {
		userRepository.deleteInBatch(users);
	}
	
}
