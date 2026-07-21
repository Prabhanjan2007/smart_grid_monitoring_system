package com.sgms.sensorservice.service;

import com.sgms.sensorservice.entity.AlertPriority;
import com.sgms.sensorservice.entity.AlertStatus;
import com.sgms.sensorservice.entity.AlertType;
import com.sgms.sensorservice.entity.SensorReading;
import com.sgms.sensorservice.exception.ResourceNotFoundException;
import com.sgms.sensorservice.model.AlertDTO;
import com.sgms.sensorservice.model.SensorReadingDTO;
import com.sgms.sensorservice.model.TransformerDTO;
import com.sgms.sensorservice.repository.SensorReadingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SensorReadingService {

    @Autowired
    private SensorReadingRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SensorReadingService.class);

    private void validateTransformer(Integer transformerId) {

        String url = "http://localhost:8081/transformers/" + transformerId;

        logger.debug("Validating Transformer with id {}",transformerId);
        try {
            restTemplate.getForObject(url, TransformerDTO.class);
        }
        catch (Exception ex) {
            logger.error("Sensor readings for transformer id {} not found",transformerId);
            throw new ResourceNotFoundException("Transformer with ID " + transformerId + " not found");
        }
        logger.info("Successfully validated transformer with ID {}", transformerId);
    }

    private void createAlert(SensorReading sensor , AlertType type , AlertPriority priority , AlertStatus status , String message){
        AlertDTO dto = new AlertDTO(sensor.getTransformerId(),type,priority,status,message);
        String url = "http://localhost:8083/alerts";

        logger.info("Creating {} alert for transformer {}", type, sensor.getTransformerId());

        try {
            restTemplate.postForObject(url, dto, AlertDTO.class);
        }
        catch (Exception ex) {
            logger.error("Unable to communicate with Alert Service");
            throw new RuntimeException("Unable to communicate with Alert Service.");
        }

        logger.info("Alert created successfully");

    }

    private void checkThresholds(SensorReading sensor){

        logger.debug("Checking threshold conditions for transformer {}", sensor.getTransformerId());
        if(sensor.getLoadPercentage() >85){
            createAlert(sensor , AlertType.OVERLOAD , AlertPriority.HIGH , AlertStatus.PENDING , "Transformer Load exceed limit");
        }
        if(sensor.getTemperature() > 90){
            createAlert(sensor , AlertType.OVERHEATING , AlertPriority.HIGH , AlertStatus.PENDING , "Transformer Temperature exceed limit");

        }
        if(sensor.getVoltage() < 180 || sensor.getVoltage() > 260){
            createAlert(sensor , AlertType.FAULT , AlertPriority.MEDIUM , AlertStatus.PENDING , "Abnormal Voltage fluctuation");
        }
    }

    private SensorReadingDTO convertToDTO(SensorReading sensor) {

        SensorReadingDTO dto = new SensorReadingDTO();

        dto.setTransformerId(sensor.getTransformerId());
        dto.setVoltage(sensor.getVoltage());
        dto.setCurrent(sensor.getCurrent());
        dto.setTemperature(sensor.getTemperature());
        dto.setLoadPercentage(sensor.getLoadPercentage());

        logger.info("Converting SensorReading to SensorReadingDTO");

        return dto;
    }

    private SensorReading convertToEntity(SensorReadingDTO dto) {

        SensorReading sensor = new SensorReading();

        sensor.setTransformerId(dto.getTransformerId());
        sensor.setVoltage(dto.getVoltage());
        sensor.setCurrent(dto.getCurrent());
        sensor.setTemperature(dto.getTemperature());
        sensor.setLoadPercentage(dto.getLoadPercentage());

        sensor.setReadingTime(LocalDateTime.now());

        logger.info("Converting SensorReadingDTO to SensorReading");

        return sensor;
    }

    public SensorReadingDTO addReading(SensorReadingDTO dto) {

        validateTransformer(dto.getTransformerId());

        SensorReading sensor = convertToEntity(dto);

        SensorReading savedReading = repository.save(sensor);

        checkThresholds(savedReading);

        logger.info("Sensor reading added successfully");

        return convertToDTO(savedReading);
    }

    public SensorReadingDTO updateReading(Integer id, SensorReadingDTO dto) {

        validateTransformer(dto.getTransformerId());

        SensorReading sensor = repository.findById(id).orElse(null);

        logger.debug("Checking if Sensor readings exist foe the given id {}",id);
        if (sensor == null) {
            logger.error("Sensor Reading with id {} not found",id);
            throw new ResourceNotFoundException("Sensor reading for sensor id : "+id+" not found");
        }

        sensor.setTransformerId(dto.getTransformerId());
        sensor.setVoltage(dto.getVoltage());
        sensor.setCurrent(dto.getCurrent());
        sensor.setTemperature(dto.getTemperature());
        sensor.setLoadPercentage(dto.getLoadPercentage());
        sensor.setReadingTime(LocalDateTime.now());

        SensorReading updatedReading = repository.save(sensor);
        logger.info("Sensor reading updated successfully");

        return convertToDTO(updatedReading);
    }

    public List<SensorReadingDTO> displayReadings() {

        List<SensorReading> readings = repository.findAll();

        List<SensorReadingDTO> dtoList = new ArrayList<>();

        for (SensorReading reading : readings) {
            dtoList.add(convertToDTO(reading));
        }

        logger.info("Displaying sensor databse");
        return dtoList;
    }

    public SensorReadingDTO findById(Integer id) {

        SensorReading sensor = repository.findById(id).orElse(null);

        logger.debug("Checking if Sensor Reading with id {} exists",id);
        if (sensor == null) {
            logger.error("Sensor reading with id {} not found",id);
            throw new ResourceNotFoundException("Sensor reading for sensor id : "+id+" not found");
        }

        logger.info("Sensor Reading with id {} nound successfully",id);
        return convertToDTO(sensor);
    }

    public List<SensorReadingDTO> findByTransformerId(Integer transformerId) {

        List<SensorReading> readings = repository.findByTransformerId(transformerId);

        List<SensorReadingDTO> dtoList = new ArrayList<>();

        for (SensorReading reading : readings) {
            dtoList.add(convertToDTO(reading));
        }

        logger.info("Displaying Sensor Readings based on the transformer id {}",transformerId);

        return dtoList;
    }

    public String deleteReading(Integer id) {

        logger.debug("Checking if Sensor reading with id {} exists",id);
        if (!repository.existsById(id)) {
            logger.error("Sensor reading with id {} not found",id);
            throw new ResourceNotFoundException("Sensor reading for sensor id : "+id+" not found");
        }

        repository.deleteById(id);
        logger.info("Sensor reading with id {} deleted successfully",id);
        return "Sensor reading deleted successfully";
    }
}