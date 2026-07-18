package com.sgms.maintenanceservice.repository;

import com.sgms.maintenanceservice.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    public List<Maintenance> findByTransformerId(Integer id);
}
