package com.sgms.sensorservice.repository;

import com.sgms.sensorservice.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Integer> {

    List<SensorReading> findByTransformerId(Integer transformerId);

}