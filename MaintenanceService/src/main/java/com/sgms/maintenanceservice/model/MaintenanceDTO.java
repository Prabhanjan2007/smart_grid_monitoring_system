package com.sgms.maintenanceservice.model;

import com.sgms.maintenanceservice.entity.MaintenanceStatus;
import com.sgms.maintenanceservice.entity.MaintenanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaintenanceDTO {

    @NotNull(message = "Transformer ID is required")
    private Integer transformerId;

    @NotNull(message = "Maintenance type is required")
    private MaintenanceType maintenanceType;

    @NotNull(message = "Maintenance status is required")
    private MaintenanceStatus maintenanceStatus;

    @NotBlank(message = "Engineer name cannot be blank")
    private String engineerName;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    private LocalDate completedDate;

    private String remarks;
}