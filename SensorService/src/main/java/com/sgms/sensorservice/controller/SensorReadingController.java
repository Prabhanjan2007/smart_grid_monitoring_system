package com.sgms.sensorservice.controller;

import com.sgms.sensorservice.model.SensorReadingDTO;
import com.sgms.sensorservice.service.SensorReadingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SensorReadingController.class);

    @PostMapping("/readings")
    public ResponseEntity<SensorReadingDTO> addReading(
            @Valid @RequestBody SensorReadingDTO dto) {

        logger.info("Adding Sensor Reading to database");
        SensorReadingDTO savedReading = service.addReading(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedReading);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorReadingDTO> updateReading(@PathVariable Integer id, @Valid @RequestBody SensorReadingDTO dto) {

        logger.info("updating sensor reading with id {} to database",id);
        SensorReadingDTO updatedReading = service.updateReading(id, dto);

        return ResponseEntity.ok(updatedReading);
    }

    @GetMapping
    public ResponseEntity<List<SensorReadingDTO>> displayReadings() {

        logger.info("diplaying sensor reading database");

        List<SensorReadingDTO> readings = service.displayReadings();

        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorReadingDTO> findById(@PathVariable Integer id) {

        logger.debug("Searching for Readings with id {}",id);

        SensorReadingDTO reading = service.findById(id);

        return ResponseEntity.ok(reading);
    }

    @GetMapping("/transformer/{transformerId}")
    public ResponseEntity<List<SensorReadingDTO>> findByTransformerId(@PathVariable Integer transformerId) {

        List<SensorReadingDTO> readings = service.findByTransformerId(transformerId);

        logger.info("Searching for sensor readings with Transformer id {}",transformerId);
        return ResponseEntity.ok(readings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReading(@PathVariable Integer id) {

        logger.info("deleting sensor readings with id {}",id);

        service.deleteReading(id);

        return ResponseEntity.ok("Sensor reading deleted successfully");
    }
}
