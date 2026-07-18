package com.SGMS.transformerservice.model;

import com.SGMS.transformerservice.entity.TransformerStatus;
import com.SGMS.transformerservice.entity.TransformerType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransformerDTO {
    @NotBlank(message = "Serial no must not be blank")
    private String serialNumber;
    @NotBlank(message = "Transformer Name must not be blank")
    private String transformerName;
    private TransformerType transformerType;
    @Positive(message = "Capacity must always be positive")
    private Float capacity;
    private String location;
    private String manufacturer;
}
