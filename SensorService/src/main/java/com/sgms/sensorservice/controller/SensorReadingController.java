package com.sgms.sensorservice.controller;

import com.sgms.sensorservice.model.SensorReadingDTO;
import com.sgms.sensorservice.service.SensorReadingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorReadingController {

    @Autowired
    private SensorReadingService service;

    @PostMapping("/readings")
    public ResponseEntity<SensorReadingDTO> addReading(
            @Valid @RequestBody SensorReadingDTO dto) {

        SensorReadingDTO savedReading = service.addReading(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedReading);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorReadingDTO> updateReading(
            @PathVariable Integer id,
            @Valid @RequestBody SensorReadingDTO dto) {

        SensorReadingDTO updatedReading = service.updateReading(id, dto);

        return ResponseEntity.ok(updatedReading);
    }

    @GetMapping
    public ResponseEntity<List<SensorReadingDTO>> displayReadings() {

        List<SensorReadingDTO> readings = service.displayReadings();

        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorReadingDTO> findById(
            @PathVariable Integer id) {

        SensorReadingDTO reading = service.findById(id);

        return ResponseEntity.ok(reading);
    }

    @GetMapping("/transformer/{transformerId}")
    public ResponseEntity<List<SensorReadingDTO>> findByTransformerId(
            @PathVariable Integer transformerId) {

        List<SensorReadingDTO> readings =
                service.findByTransformerId(transformerId);

        return ResponseEntity.ok(readings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReading(
            @PathVariable Integer id) {

        service.deleteReading(id);

        return ResponseEntity.ok("Sensor reading deleted successfully");
    }
}
