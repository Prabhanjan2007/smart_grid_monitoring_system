package com.sgms.maintenanceservice.model;


import com.sgms.maintenanceservice.entity.AlertPriority;
import com.sgms.maintenanceservice.entity.AlertStatus;
import com.sgms.maintenanceservice.entity.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AlertDTO {

    @NotNull(message = "Transformer ID is required")
    private Integer transformerId;

    @NotNull(message = "Alert Type is required")
    private AlertType alertType;

    @NotNull(message = "Priority is required")
    private AlertPriority priority;

    @NotNull(message = "Status is required")
    private AlertStatus status;

    @NotBlank(message = "Alert message cannot be blank")
    private String message;
}

