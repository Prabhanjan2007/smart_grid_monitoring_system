package com.sgms.maintenanceservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maintenanceId;

    @Column(nullable = false)
    private Integer transformerId;

    @Enumerated(EnumType.STRING)
    private MaintenanceType maintenanceType;

    @Enumerated(EnumType.STRING)
    private  MaintenanceStatus maintenanceStatus;

    @Column(nullable = false)
    private String engineerName;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    private LocalDate completedDate;

    @Column(length = 500)
    private String remarks;
}
