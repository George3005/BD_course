package com.george.AutoServiceERP.repository;

import com.george.AutoServiceERP.model.Master;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterRepository extends JpaRepository<Master, Long> {
    Optional<Master> findByName(String name);
}