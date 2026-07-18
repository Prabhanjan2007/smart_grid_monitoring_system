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

    private void validateTransformer(Integer transformerId) {

        String url = "http://localhost:8081/transformers/" + transformerId;

        try {
            restTemplate.getForObject(url, TransformerDTO.class);
        }
        catch (Exception ex) {
            throw new ResourceNotFoundException("Transformer with ID " + transformerId + " not found");
        }
    }

    private void createAlert(SensorReading sensor , AlertType type , AlertPriority priority , AlertStatus status , String message){
        AlertDTO dto = new AlertDTO(sensor.getTransformerId(),type,priority,status,message);
        String url = "http://localhost:8083/alerts";
        try {
            restTemplate.postForObject(url, dto, AlertDTO.class);
        }
        catch (Exception ex) {
            throw new RuntimeException("Unable to communicate with Alert Service.");
        }

    }

    private void checkThresholds(SensorReading sensor){
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

        return sensor;
    }

    public SensorReadingDTO addReading(SensorReadingDTO dto) {

        validateTransformer(dto.getTransformerId());

        SensorReading sensor = convertToEntity(dto);

        SensorReading savedReading = repository.save(sensor);

        checkThresholds(savedReading);

        return convertToDTO(savedReading);
    }

    public SensorReadingDTO updateReading(Integer id, SensorReadingDTO dto) {

        validateTransformer(dto.getTransformerId());

        SensorReading sensor = repository.findById(id).orElse(null);

        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor reading for sensor id : "+id+" not found");
        }

        sensor.setTransformerId(dto.getTransformerId());
        sensor.setVoltage(dto.getVoltage());
        sensor.setCurrent(dto.getCurrent());
        sensor.setTemperature(dto.getTemperature());
        sensor.setLoadPercentage(dto.getLoadPercentage());
        sensor.setReadingTime(LocalDateTime.now());

        SensorReading updatedReading = repository.save(sensor);

        return convertToDTO(updatedReading);
    }

    public List<SensorReadingDTO> displayReadings() {

        List<SensorReading> readings = repository.findAll();

        List<SensorReadingDTO> dtoList = new ArrayList<>();

        for (SensorReading reading : readings) {
            dtoList.add(convertToDTO(reading));
        }

        return dtoList;
    }

    public SensorReadingDTO findById(Integer id) {

        SensorReading sensor = repository.findById(id).orElse(null);

        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor reading for sensor id : "+id+" not found");
        }

        return convertToDTO(sensor);
    }

    public List<SensorReadingDTO> findByTransformerId(Integer transformerId) {

        List<SensorReading> readings = repository.findByTransformerId(transformerId);

        List<SensorReadingDTO> dtoList = new ArrayList<>();

        for (SensorReading reading : readings) {
            dtoList.add(convertToDTO(reading));
        }

        return dtoList;
    }

    public String deleteReading(Integer id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sensor reading for sensor id : "+id+" not found");
        }

        repository.deleteById(id);

        return "Sensor reading deleted successfully";
    }
}