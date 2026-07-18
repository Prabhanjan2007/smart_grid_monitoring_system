package com.sgms.sensorservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sensorId;

    @Column(nullable = false)
    private Integer transformerId;

    @Column(nullable = false)
    private Float voltage;

    @Column(nullable = false)
    private Float current;

    @Column(nullable = false)
    private Float temperature;

    @Column(nullable = false)
    private Float loadPercentage;

    @Column(nullable = false)
    private LocalDateTime readingTime;
}