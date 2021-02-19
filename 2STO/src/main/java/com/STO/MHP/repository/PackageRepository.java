package com.STO.MHP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.STO.MHP.models.Package;


@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

	//User findByEmail(String email);
}
