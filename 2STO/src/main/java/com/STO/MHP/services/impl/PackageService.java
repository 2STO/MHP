package com.STO.MHP.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.STO.MHP.models.Package;
import com.STO.MHP.repository.PackageRepository;
import com.STO.MHP.services.IPackageService;




@Service
public class PackageService implements IPackageService {
	
	@Autowired
	private PackageRepository packageRepository;
	
	@Override
	public Package save(Package entity) {
		return packageRepository.save(entity);
	}

	@Override
	public Package update(Package entity) {
		return packageRepository.save(entity);
	}

	@Override
	public void delete(Package entity) {
		packageRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		packageRepository.deleteById(id);
	}

	@Override
	public Package find(Long id) {
		return packageRepository.findById(id).orElse(null);
	}

	@Override
	public List<Package> findAll() {
		return packageRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Package> event) {
		packageRepository.deleteInBatch(event);
	}


	
}
