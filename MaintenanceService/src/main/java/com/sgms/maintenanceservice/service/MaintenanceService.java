package com.sgms.maintenanceservice.service;

import com.sgms.maintenanceservice.entity.Maintenance;
import com.sgms.maintenanceservice.entity.MaintenanceStatus;
import com.sgms.maintenanceservice.exception.ResourceNotFoundException;
import com.sgms.maintenanceservice.model.AlertDTO;
import com.sgms.maintenanceservice.model.AlertResponseDTO;
import com.sgms.maintenanceservice.model.MaintenanceDTO;
import com.sgms.maintenanceservice.model.TransformerDTO;
import com.sgms.maintenanceservice.repository.MaintenanceRepository;
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

    private void validateTransformer(Integer transformerId) {

        String url = "http://localhost:8081/transformers/" + transformerId;

        try {
            restTemplate.getForObject(url, TransformerDTO.class);
        }
        catch (Exception ex) {
            throw new ResourceNotFoundException("Transformer with ID " + transformerId + " not found");
        }
    }

    private void resolveAlerts(MaintenanceDTO maintenance){

        String urlGet = "http://localhost:8083/alerts/transformer/" + maintenance.getTransformerId();

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

        return maintenance;
    }

    public MaintenanceDTO addMaintenance(MaintenanceDTO dto) {

        validateTransformer(dto.getTransformerId());

        Maintenance maintenance = convertToEntity(dto);

        Maintenance savedMaintenance = repository.save(maintenance);

        if(savedMaintenance.getMaintenanceStatus() == MaintenanceStatus.COMPLETED){
            resolveAlerts(convertToDTO(savedMaintenance));
        }

        return convertToDTO(savedMaintenance);
    }

    public MaintenanceDTO updateMaintenance(Integer id, MaintenanceDTO dto) {

        validateTransformer(dto.getTransformerId());

        Maintenance maintenance = repository.findById(id).orElse(null);

        if (maintenance == null) {
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

        return convertToDTO(updatedMaintenance);
    }

    public List<MaintenanceDTO> displayMaintenances() {

        List<Maintenance> maintenanceList = repository.findAll();

        List<MaintenanceDTO> dtoList = new ArrayList<>();

        for (Maintenance maintenance : maintenanceList) {
            dtoList.add(convertToDTO(maintenance));
        }

        return dtoList;
    }

    public MaintenanceDTO findById(Integer id) {

        Maintenance maintenance = repository.findById(id).orElse(null);

        if (maintenance == null) {
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        return convertToDTO(maintenance);
    }

    public List<MaintenanceDTO> findByTransformerId(Integer transformerId) {

        List<Maintenance> maintenanceList = repository.findByTransformerId(transformerId);

        List<MaintenanceDTO> dtoList = new ArrayList<>();

        for (Maintenance maintenance : maintenanceList) {
            dtoList.add(convertToDTO(maintenance));
        }

        return dtoList;
    }

    public MaintenanceDTO startMaintenance(Integer id) {

        Maintenance maintenance = repository.findById(id).orElse(null);

        if (maintenance == null) {
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        maintenance.setMaintenanceStatus(MaintenanceStatus.IN_PROGRESS);

        Maintenance updatedMaintenance = repository.save(maintenance);

        return convertToDTO(updatedMaintenance);
    }

    public MaintenanceDTO completeMaintenance(Integer id) {

        Maintenance maintenance = repository.findById(id).orElse(null);

        if (maintenance == null) {
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        maintenance.setMaintenanceStatus(MaintenanceStatus.COMPLETED);

        Maintenance updatedMaintenance = repository.save(maintenance);

        return convertToDTO(updatedMaintenance);
    }

    public String deleteMaintenance(Integer id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance record not found");
        }

        repository.deleteById(id);

        return "Maintenance record deleted successfully";
    }
}