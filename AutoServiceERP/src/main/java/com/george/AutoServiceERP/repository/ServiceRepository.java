package com.george.AutoServiceERP.repository;

import com.george.AutoServiceERP.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByName(String name);
}

