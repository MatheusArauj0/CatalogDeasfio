package com.matheus.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheus.catalog.entities.AuditDetails;

@Repository
public interface AuditDetaisRepository extends JpaRepository<AuditDetails, Long>{

}
