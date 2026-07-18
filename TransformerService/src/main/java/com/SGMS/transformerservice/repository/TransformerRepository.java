package com.SGMS.transformerservice.repository;

import com.SGMS.transformerservice.entity.Transformer;
import com.SGMS.transformerservice.entity.TransformerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransformerRepository extends JpaRepository<Transformer, Integer> {
    boolean existsBySerialNumber(String serialNumber);
    Transformer findBySerialNumber(String serialNumber);
    List<Transformer> findByTransformerStatus(TransformerStatus transformerStatus);
}

