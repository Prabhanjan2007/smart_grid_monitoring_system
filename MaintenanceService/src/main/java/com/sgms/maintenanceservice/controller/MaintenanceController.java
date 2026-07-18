package com.sgms.maintenanceservice.controller;

import com.sgms.maintenanceservice.model.MaintenanceDTO;
import com.sgms.maintenanceservice.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService service;

    @PostMapping
    public ResponseEntity<MaintenanceDTO> addMaintenance( @Valid @RequestBody MaintenanceDTO dto) {

        MaintenanceDTO savedMaintenance = service.addMaintenance(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMaintenance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> updateMaintenance(@PathVariable Integer id, @Valid @RequestBody MaintenanceDTO dto) {

        MaintenanceDTO updatedMaintenance = service.updateMaintenance(id, dto);

        return ResponseEntity.ok(updatedMaintenance);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> displayMaintenances() {

        List<MaintenanceDTO> maintenances = service.displayMaintenances();

        return ResponseEntity.ok(maintenances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> findById(@PathVariable Integer id) {

        MaintenanceDTO maintenance = service.findById(id);

        return ResponseEntity.ok(maintenance);
    }

    @GetMapping("/transformer/{transformerId}")
    public ResponseEntity<List<MaintenanceDTO>> findByTransformerId(@PathVariable Integer transformerId) {

        List<MaintenanceDTO> maintenances = service.findByTransformerId(transformerId);

        return ResponseEntity.ok(maintenances);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<MaintenanceDTO> startMaintenance( @PathVariable Integer id) {

        MaintenanceDTO maintenance = service.startMaintenance(id);

        return ResponseEntity.ok(maintenance);
    }

    // COMPLETE MAINTENANCE
    @PutMapping("/{id}/complete")
    public ResponseEntity<MaintenanceDTO> completeMaintenance(@PathVariable Integer id) {

        MaintenanceDTO maintenance = service.completeMaintenance(id);

        return ResponseEntity.ok(maintenance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMaintenance(@PathVariable Integer id) {

        service.deleteMaintenance(id);

        return ResponseEntity.ok("Maintenance record deleted successfully");
    }
}