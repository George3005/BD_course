package com.george.AutoServiceERP.service;

import com.george.AutoServiceERP.repository.MasterRepository;
import com.george.AutoServiceERP.exception.ResourceNotFoundException;
import com.george.AutoServiceERP.model.Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterService {
    private final MasterRepository masterRepository;

    @Autowired
    public MasterService(MasterRepository masterRepository) {
        this.masterRepository = masterRepository;
    }

    public Master saveMaster(Master master) {
        return masterRepository.save(master);
    }

    public List<Master> getAllMasters() {
        return masterRepository.findAll();
    }

    public Master updateMaster(Long id, Master masterDetails) {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id : " + id));
        master.setName(masterDetails.getName());
        // другие поля для обновления
        return masterRepository.save(master);
    }

    public ResponseEntity<?> deleteMaster(Long id) {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Master not found with id : " + id));
        masterRepository.delete(master);
        return ResponseEntity.ok().build();
    }
}