package com.sgms.alertservice.service;

import com.sgms.alertservice.entity.*;
import com.sgms.alertservice.exception.ResourceNotFoundException;
import com.sgms.alertservice.model.AlertDTO;
import com.sgms.alertservice.model.MaintenanceDTO;
import com.sgms.alertservice.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    private AlertRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private void createMaintenance(Integer transformerId , MaintenanceType type , MaintenanceStatus status ){

        String url = "http://localhost:8084/maintenance";

        MaintenanceDTO dto = new MaintenanceDTO();

        dto.setTransformerId(transformerId);
        dto.setMaintenanceType(type);
        dto.setMaintenanceStatus(status);
        dto.setEngineerName("Not Assigned");
        dto.setScheduledDate(LocalDate.now());

        restTemplate.postForObject(url,dto,MaintenanceDTO.class);
    }

    private AlertDTO convertToDTO(Alert alert){

        AlertDTO dto = new AlertDTO();

        dto.setTransformerId(alert.getTransformerId());
        dto.setAlertType(alert.getAlertType());
        dto.setPriority(alert.getPriority());
        dto.setStatus(alert.getStatus());
        dto.setMessage(alert.getMessage());


        return dto;
    }

    private Alert convertToEntity(AlertDTO dto){

        Alert alert = new Alert();

        alert.setTransformerId(dto.getTransformerId());
        alert.setAlertType(dto.getAlertType());
        alert.setPriority(dto.getPriority());
        alert.setStatus(AlertStatus.PENDING);
        alert.setMessage(dto.getMessage());
        alert.setAlertTime(LocalDateTime.now());

        return alert;
    }

    public AlertDTO addAlert(AlertDTO dto){

        Alert alert = convertToEntity(dto);

        Alert savedAlert = repository.save(alert);

        if(savedAlert.getPriority() == AlertPriority.HIGH ){
            createMaintenance(dto.getTransformerId() , MaintenanceType.EMERGENCY , MaintenanceStatus.SCHEDULED );
        }

        return convertToDTO(savedAlert);
    }

    public AlertDTO updateAlert(Integer id, AlertDTO dto){

        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            throw new ResourceNotFoundException("Alert not found");
        }

        alert.setTransformerId(dto.getTransformerId());
        alert.setAlertType(dto.getAlertType());
        alert.setPriority(dto.getPriority());
        alert.setStatus(AlertStatus.PENDING);
        alert.setMessage(dto.getMessage());
        alert.setAlertTime(LocalDateTime.now());

        Alert updatedAlert = repository.save(alert);

        if(updatedAlert.getPriority() == AlertPriority.HIGH ){
            createMaintenance(dto.getTransformerId() , MaintenanceType.EMERGENCY , MaintenanceStatus.SCHEDULED );
        }

        return convertToDTO(updatedAlert);
    }

    public List<AlertDTO> displayAlerts(){

        List<Alert> alerts = repository.findAll();

        List<AlertDTO> dtoList = new ArrayList<>();

        for(Alert alert : alerts){
            dtoList.add(convertToDTO(alert));
        }

        return dtoList;
    }

    public AlertDTO findById(Integer id){

        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            throw new ResourceNotFoundException("Alert not found");
        }

        return convertToDTO(alert);
    }

    public List<AlertDTO> findByTransformerId(Integer transformerId){

        List<Alert> alerts = repository.findByTransformerId(transformerId);

        List<AlertDTO> dtoList = new ArrayList<>();

        for(Alert alert : alerts){
            dtoList.add(convertToDTO(alert));
        }

        return dtoList;
    }

    public List<Alert> findAlertByTransformerId(Integer transformerId){

        List<Alert> alerts = repository.findByTransformerId(transformerId);

        return alerts;
    }

    public AlertDTO acknowledgeAlert(Integer id){

        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            throw new ResourceNotFoundException("Alert not found");
        }

        alert.setStatus(AlertStatus.ACKNOWLEDGED);

        Alert updatedAlert = repository.save(alert);

        return convertToDTO(updatedAlert);
    }

    public AlertDTO resolveAlert(Integer id){

        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            throw new ResourceNotFoundException("Alert not found");
        }

        alert.setStatus(AlertStatus.RESOLVED);

        Alert updatedAlert = repository.save(alert);

        return convertToDTO(updatedAlert);
    }

    public String deleteAlert(Integer id){

        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Alert not found");
        }

        repository.deleteById(id);

        return "Alert deleted successfully";
    }

}