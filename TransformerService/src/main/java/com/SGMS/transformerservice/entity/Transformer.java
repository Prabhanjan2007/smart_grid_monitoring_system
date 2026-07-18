package com.SGMS.transformerservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Entity
@Table(name = "transformers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transformer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer transformerId;
    @Column(unique = true , nullable = false)
    private String serialNumber;
    @Column(nullable = false)
    private String transformerName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransformerType transformerType;
    @Positive
    @Column(nullable = false)
    private Float capacity;
    @Column(nullable = false)
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransformerStatus transformerStatus;
    @Column(nullable = false)
    private String manufacturer;
}
