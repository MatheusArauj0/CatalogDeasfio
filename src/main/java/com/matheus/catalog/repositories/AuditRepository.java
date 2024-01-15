package com.matheus.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheus.catalog.entities.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
	
	Audit findByName(String name);
}	

