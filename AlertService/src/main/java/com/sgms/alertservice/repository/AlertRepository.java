package com.sgms.alertservice.repository;

import com.sgms.alertservice.entity.Alert;
import com.sgms.alertservice.entity.AlertPriority;
import com.sgms.alertservice.entity.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Integer> {

    List<Alert> findByTransformerId(Integer transformerId);

    List<Alert> findByStatus(AlertStatus status);

    List<Alert> findByPriority(AlertPriority priority);

}