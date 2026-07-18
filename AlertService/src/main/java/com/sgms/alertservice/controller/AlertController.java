package com.sgms.alertservice.controller;

import com.sgms.alertservice.entity.Alert;
import com.sgms.alertservice.model.AlertDTO;
import com.sgms.alertservice.service.AlertService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<AlertDTO> addAlert(@Valid @RequestBody AlertDTO dto) {

        AlertDTO savedAlert = service.addAlert(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAlert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertDTO> updateAlert(@PathVariable Integer id, @Valid @RequestBody AlertDTO dto) {

        AlertDTO updatedAlert = service.updateAlert(id, dto);

        return ResponseEntity.ok(updatedAlert);
    }

    @GetMapping
    public ResponseEntity<List<AlertDTO>> displayAlerts() {

        List<AlertDTO> alerts = service.displayAlerts();

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertDTO> findById(@PathVariable Integer id) {

        AlertDTO alert = service.findById(id);

        return ResponseEntity.ok(alert);
    }

    @GetMapping("/transformer/{transformerId}")
    public ResponseEntity<List<AlertDTO>> findByTransformerId(@PathVariable Integer transformerId) {

        List<AlertDTO> alerts = service.findByTransformerId(transformerId);

        return ResponseEntity.ok(alerts);
    }


    @GetMapping("/transformer/alert/{transformerId}")
    public ResponseEntity<List<Alert>> findAlertByTransformerId(@PathVariable Integer transformerId) {

        List<Alert> alerts = service.findAlertByTransformerId(transformerId);

        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<AlertDTO> acknowledgeAlert(@PathVariable Integer id) {

        AlertDTO alert = service.acknowledgeAlert(id);

        return ResponseEntity.ok(alert);
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<AlertDTO> resolveAlert(@PathVariable Integer id) {

        AlertDTO alert = service.resolveAlert(id);

        return ResponseEntity.ok(alert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable Integer id) {

        service.deleteAlert(id);

        return ResponseEntity.ok("Alert deleted successfully");
    }

}