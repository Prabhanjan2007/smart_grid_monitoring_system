package com.sgms.sensorservice.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensorReadingDTO {

    @NotNull(message = "Transformer ID is required")
    private Integer transformerId;

    @NotNull(message = "Voltage is required")
    @Positive(message = "Voltage must be greater than 0")
    private Float voltage;

    @NotNull(message = "Current is required")
    @Positive(message = "Current must be greater than 0")
    private Float current;

    @NotNull(message = "Temperature is required")
    @Min(value = -40, message = "Temperature cannot be below -40°C")
    @Max(value = 200, message = "Temperature cannot exceed 200°C")
    private Float temperature;

    @NotNull(message = "Load Percentage is required")
    @Min(value = 0, message = "Load Percentage cannot be below 0")
    @Max(value = 100, message = "Load Percentage cannot exceed 100")
    private Float loadPercentage;
}