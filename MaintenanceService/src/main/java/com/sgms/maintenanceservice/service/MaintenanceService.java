package com.sgms.maintenanceservice.service;

import com.sgms.maintenanceservice.entity.Maintenance;
import com.sgms.maintenanceservice.entity.MaintenanceStatus;
import com.sgms.maintenanceservice.exception.ResourceNotFoundException;
import com.sgms.maintenanceservice.model.AlertDTO;
import com.sgms.maintenanceservice.model.AlertResponseDTO;
import com.sgms.maintenanceservice.model.MaintenanceDTO;
import com.sgms.maintenanceservice.model.TransformerDTO;
import com.sgms.maintenanceservice.repository.MaintenanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceService.class);

    private void validateTransformer(Integer transformerId) {

        logger.info("Validating the transformer with transformer id {}",transformerId);

        String url = "http://localhost:8081/transformers/" + transformerId;

        try {
            restTemplate.getForObject(url, TransformerDTO.class);
        }
        catch (Exception ex) {
            logger.error("Transformer with transformer id {} not found",transformerId);
            throw new ResourceNotFoundException("Transformer with ID " + transformerId + " not found");
        }
    }

    private void resolveAlerts(MaintenanceDTO maintenance){

        String urlGet = "http://localhost:8083/alerts/transformer/" + maintenance.getTransformerId();

        logger.info("Setting Alert status to COMPLETED");

        AlertResponseDTO[] alerts = restTemplate.getForObject(urlGet,AlertResponseDTO[].class);

        for(AlertResponseDTO dto : alerts){
            String urlPatch = "http://localhost:8083/alerts/"+ dto.getAlertId() + "/resolve";
            if(maintenance.getMaintenanceStatus() != MaintenanceStatus.COMPLETED){
                restTemplate.put(urlPatch , null);
            }
        }
    }

    private MaintenanceDTO convertToDTO(Maintenance maintenance) {

        MaintenanceDTO dto = new MaintenanceDTO();

        dto.setTransformerId(maintenance.getTransformerId());
        dto.setMaintenanceType(maintenance.getMaintenanceType());
        dto.setMaintenanceStatus(maintenance.getMaintenanceStatus());
        dto.setEngineerName(maintenance.getEngineerName());
        dto.setScheduledDate(maintenance.getScheduledDate());
        dto.setCompletedDate(maintenance.getCompletedDate());
        dto.setRemarks(maintenance.getRemarks());

        logger.info("Converting Maintenance to MaintenanceDTO");

        return dto;
    }

    private Maintenance convertToEntity(MaintenanceDTO dto) {

        Maintenance maintenance = new Maintenance();

        maintenance.setTransformerId(dto.getTransformerId());
        maintenance.setMaintenanceType(dto.getMaintenanceType());
        maintenance.setMaintenanceStatus(MaintenanceStatus.SCHEDULED);
        maintenance.setEngineerName(dto.getEngineerName());
        maintenance.setScheduledDate(dto.getScheduledDate());
        maintenance.setCompletedDate(dto.getCompletedDate());
        maintenance.setRemarks(dto.getRemarks());

        logger.info("Converting MaintenanceDTO to Maintenance");

        return maintenance;
    }

    public MaintenanceDTO addMaintenance(MaintenanceDTO dto) {

        validateTransformer(dto.getTransformerId());

        Maintenance maintenance = convertToEntity(dto);

        Maintenance savedMaintenance = repository.save(maintenance);

        logger.info("Maintenance added successfully");
        if(savedMaintenance.getMaintenanceStatus() == MaintenanceStatus.COMPLETED){
            resolveAlerts(convertToDTO(savedMaintenance));
        }

        return convertToDTO(savedMaintenance);
    }

    public MaintenanceDTO updateMaintenance(Integer id, MaintenanceDTO dto) {

        validateTransformer(dto.getTransformerId());

        logger.info("Searching Maintenance by Id {}",id);
        Maintenance maintenance = repository.findById(id).orElse(null);

        if (maintenance == null) {
            logger.error("Record not found for id {}",id);
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        maintenance.setTransformerId(dto.getTransformerId());
        maintenance.setMaintenanceType(dto.getMaintenanceType());
        maintenance.setMaintenanceStatus(MaintenanceStatus.SCHEDULED);
        maintenance.setEngineerName(dto.getEngineerName());
        maintenance.setScheduledDate(dto.getScheduledDate());
        maintenance.setCompletedDate(dto.getCompletedDate());
        maintenance.setRemarks(dto.getRemarks());

        Maintenance updatedMaintenance = repository.save(maintenance);

        if(updatedMaintenance.getMaintenanceStatus() == MaintenanceStatus.COMPLETED){
            resolveAlerts(convertToDTO(updatedMaintenance));
        }

        logger.info("Record updated successfully for id {}",id);
        return convertToDTO(updatedMaintenance);
    }

    public List<MaintenanceDTO> displayMaintenances() {

        List<Maintenance> maintenanceList = repository.findAll();

        List<MaintenanceDTO> dtoList = new ArrayList<>();

        for (Maintenance maintenance : maintenanceList) {
            dtoList.add(convertToDTO(maintenance));
        }

        logger.info("Displaying maintenance records from database");
        return dtoList;
    }

    public MaintenanceDTO findById(Integer id) {

        Maintenance maintenance = repository.findById(id).orElse(null);

        logger.info("Searching Maintenance by id {}",id);
        if (maintenance == null) {
            logger.error("Maintenance record not found for id {}",id);
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        logger.info("Displaying Maintenance record in id {}",id);
        return convertToDTO(maintenance);
    }

    public List<MaintenanceDTO> findByTransformerId(Integer transformerId) {

        List<Maintenance> maintenanceList = repository.findByTransformerId(transformerId);

        logger.info("Searching Maintenance by transformer id {}",transformerId);
        List<MaintenanceDTO> dtoList = new ArrayList<>();

        for (Maintenance maintenance : maintenanceList) {
            dtoList.add(convertToDTO(maintenance));
        }

        logger.info("Displaying Maintenance record in transformer id {}",transformerId);
        return dtoList;
    }

    public MaintenanceDTO startMaintenance(Integer id) {

        Maintenance maintenance = repository.findById(id).orElse(null);

        if (maintenance == null) {
            logger.error("Maintenance record not found for id {}",id);
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        maintenance.setMaintenanceStatus(MaintenanceStatus.IN_PROGRESS);

        Maintenance updatedMaintenance = repository.save(maintenance);

        logger.info("Maintenance record for id {} set to IN_PROGRESS");
        return convertToDTO(updatedMaintenance);
    }

    public MaintenanceDTO completeMaintenance(Integer id) {

        Maintenance maintenance = repository.findById(id).orElse(null);

        if (maintenance == null) {
            logger.error("Maintenance not found for id {}",id);
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        maintenance.setMaintenanceStatus(MaintenanceStatus.COMPLETED);

        Maintenance updatedMaintenance = repository.save(maintenance);

        logger.info("Maintenance set to COMPLETED Successfully");
        return convertToDTO(updatedMaintenance);
    }

    public String deleteMaintenance(Integer id) {

        if (!repository.existsById(id)) {
            logger.error("Maintenance not found for id {}",id);
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        repository.deleteById(id);
        logger.info("Record Deleted successfully for id {}",id);
        return "Maintenance record deleted successfully";
    }
}