package com.sgms.alertservice.controller;

import com.sgms.alertservice.entity.Alert;
import com.sgms.alertservice.model.AlertDTO;
import com.sgms.alertservice.service.AlertService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService service;

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    @PostMapping
    public ResponseEntity<AlertDTO> addAlert(@Valid @RequestBody AlertDTO dto) {

        AlertDTO savedAlert = service.addAlert(dto);
        logger.info("Alert Added to records successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertDTO> updateAlert(@PathVariable Integer id, @Valid @RequestBody AlertDTO dto) {

        AlertDTO updatedAlert = service.updateAlert(id, dto);

        logger.info("Alert record with id {} was updated successfully",id);

        return ResponseEntity.ok(updatedAlert);
    }

    @GetMapping
    public ResponseEntity<List<AlertDTO>> displayAlerts() {

        List<AlertDTO> alerts = service.displayAlerts();

        logger.info("Displaying all the records in theMaintenance database");

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> findById(@PathVariable Integer id) {

        logger.info("Searching alert by id {}",id);

        AlertDTO alert = service.findById(id);

        return ResponseEntity.ok(alert);
    }

    @GetMapping("/transformer/{transformerId}")
    public ResponseEntity<List<AlertDTO>> findByTransformerId(@PathVariable Integer transformerId) {

        logger.info("Searching alertDTO by transformer id {}",transformerId);

        List<AlertDTO> alerts = service.findByTransformerId(transformerId);

        return ResponseEntity.ok(alerts);
    }


    @GetMapping("/transformer/alert/{transformerId}")
    public ResponseEntity<List<Alert>> findAlertByTransformerId(@PathVariable Integer transformerId) {

        logger.info("Searching alerts by transformer id {}",transformerId);

        List<Alert> alerts = service.findAlertByTransformerId(transformerId);

        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<AlertDTO> acknowledgeAlert(@PathVariable Integer id) {

        logger.info("Alert with id {} is being acknowledged",id);

        AlertDTO alert = service.acknowledgeAlert(id);

        return ResponseEntity.ok(alert);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<AlertDTO> resolveAlert(@PathVariable Integer id) {

        logger.info("Alert with id {} is being resolved",id);

        AlertDTO alert = service.resolveAlert(id);

        return ResponseEntity.ok(alert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable Integer id) {

        logger.info("Alert with id {} is being deleted",id);

        service.deleteAlert(id);

        return ResponseEntity.ok("Alert deleted successfully");
    }

}