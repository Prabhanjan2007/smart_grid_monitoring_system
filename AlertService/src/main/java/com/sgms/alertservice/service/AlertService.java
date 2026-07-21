package com.sgms.alertservice.service;

import com.sgms.alertservice.entity.*;
import com.sgms.alertservice.exception.ResourceNotFoundException;
import com.sgms.alertservice.model.AlertDTO;
import com.sgms.alertservice.model.MaintenanceDTO;
import com.sgms.alertservice.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    private void createMaintenance(Integer transformerId , MaintenanceType type , MaintenanceStatus status ){

        String url = "http://localhost:8084/maintenance";

        logger.info("Maintenance with transformer id {} is created",transformerId);

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

        logger.info("Alert is converted to AlertDTO");

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

        logger.info("AlertDTO is converted to Alert");
        return alert;
    }

    public AlertDTO addAlert(AlertDTO dto){

        Alert alert = convertToEntity(dto);

        Alert savedAlert = repository.save(alert);
        logger.info("Alert saved in the alert database");
        if(savedAlert.getPriority() == AlertPriority.HIGH ){
            logger.info("Creating a Maintenance for High Priority");
            createMaintenance(dto.getTransformerId() , MaintenanceType.EMERGENCY , MaintenanceStatus.SCHEDULED );
        }

        return convertToDTO(savedAlert);
    }

    public AlertDTO updateAlert(Integer id, AlertDTO dto){

        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            logger.error("Alert not found for the given id {}",id);
            throw new ResourceNotFoundException("Alert not found");
        }

        alert.setTransformerId(dto.getTransformerId());
        alert.setAlertType(dto.getAlertType());
        alert.setPriority(dto.getPriority());
        alert.setStatus(AlertStatus.PENDING);
        alert.setMessage(dto.getMessage());
        alert.setAlertTime(LocalDateTime.now());

        Alert updatedAlert = repository.save(alert);
        logger.info("Alert in id {} updated successfully",id);

        if(updatedAlert.getPriority() == AlertPriority.HIGH ){
            logger.info("Creating a Maintenance for High Priority");
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

        logger.info("Displaying all the elements present in the Alert database");

        return dtoList;
    }

    public AlertDTO findById(Integer id){

        logger.debug("Searching for alert with id {}",id);
        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            logger.error("The alert record with given id {} not found",id);
            throw new ResourceNotFoundException("Alert not found");
        }

        logger.info("The alert record with id {} found and displayed",id);
        return convertToDTO(alert);
    }

    public List<AlertDTO> findByTransformerId(Integer transformerId){

        logger.debug("Searching for Alert record with Transformer id {}",transformerId);
        List<Alert> alerts = repository.findByTransformerId(transformerId);

        List<AlertDTO> dtoList = new ArrayList<>();

        for(Alert alert : alerts){
            dtoList.add(convertToDTO(alert));
        }

        logger.info("Alert records with transformer id {} is being displayed",transformerId);
        return dtoList;
    }

    public List<Alert> findAlertByTransformerId(Integer transformerId){

        List<Alert> alerts = repository.findByTransformerId(transformerId);
        logger.info("Displaying all alerts with transformer id {}",transformerId);

        return alerts;
    }

    public AlertDTO acknowledgeAlert(Integer id){

        logger.debug("ALert with id {} is being searched",id);
        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            logger.error("Alert with id {} not found",id);
            throw new ResourceNotFoundException("Alert not found");
        }

        alert.setStatus(AlertStatus.ACKNOWLEDGED);

        Alert updatedAlert = repository.save(alert);

        logger.info("Alert status is set to Acknowledged and saved");

        return convertToDTO(updatedAlert);
    }

    public AlertDTO resolveAlert(Integer id){

        Alert alert = repository.findById(id).orElse(null);

        if(alert == null){
            logger.error("Alert with id {} not found",id);
            throw new ResourceNotFoundException("Alert not found");
        }

        alert.setStatus(AlertStatus.RESOLVED);

        Alert updatedAlert = repository.save(alert);

        logger.info("Alert status is set to Resolved and saved");
        return convertToDTO(updatedAlert);
    }

    public String deleteAlert(Integer id){

        if(!repository.existsById(id)){
            logger.error("Alert with the given id {} not found",id);
            throw new ResourceNotFoundException("Alert not found");
        }

        logger.info("Alert deleted Successfully");
        repository.deleteById(id);

        return "Alert deleted successfully";
    }

}