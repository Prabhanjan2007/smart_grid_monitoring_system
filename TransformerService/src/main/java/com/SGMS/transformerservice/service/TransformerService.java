package com.SGMS.transformerservice.service;

import com.SGMS.transformerservice.entity.Transformer;
import com.SGMS.transformerservice.entity.TransformerStatus;
import com.SGMS.transformerservice.exception.DuplicateResourceException;
import com.SGMS.transformerservice.exception.ResourceNotFoundException;
import com.SGMS.transformerservice.model.TransformerDTO;
import com.SGMS.transformerservice.repository.TransformerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransformerService {
    @Autowired
    private TransformerRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(TransformerService.class);

    private TransformerDTO convertToDTO(Transformer transformer){

        logger.info("Converting Transformer to TransformerDTO");

        TransformerDTO transformerDTO = new TransformerDTO();
        transformerDTO.setTransformerName(transformer.getTransformerName());
        transformerDTO.setCapacity(transformer.getCapacity());
        transformerDTO.setTransformerType(transformer.getTransformerType());
        transformerDTO.setSerialNumber(transformer.getSerialNumber());
        transformerDTO.setLocation(transformer.getLocation());
        transformerDTO.setManufacturer(transformer.getManufacturer());

        return transformerDTO;
    }

    private Transformer convertToEntity(TransformerDTO dto){

        Transformer transformer = new Transformer();

        logger.info("Converting TransformerDTO to Transformer");

        transformer.setSerialNumber(dto.getSerialNumber());
        transformer.setTransformerName(dto.getTransformerName());
        transformer.setTransformerType(dto.getTransformerType());
        transformer.setCapacity(dto.getCapacity());
        transformer.setLocation(dto.getLocation());
        transformer.setManufacturer(dto.getManufacturer());

        transformer.setTransformerStatus(TransformerStatus.ACTIVE);

        return transformer;
    }

    public TransformerDTO addTransformer(TransformerDTO transformerDTO){

        Transformer transformer = convertToEntity(transformerDTO);

        logger.debug("Checking if serial number {} already exist",transformerDTO.getSerialNumber());
        if(repository.existsBySerialNumber(transformerDTO.getSerialNumber())){
            logger.error("Transformer with serial number {} already exists",transformerDTO.getSerialNumber());
            throw new DuplicateResourceException("Serial Number : "+ transformerDTO.getSerialNumber() +" already exists");
        }

        Transformer savedTransformer = repository.save(transformer);
        logger.info("Transformer {} saved successfully",savedTransformer.getSerialNumber());
        return convertToDTO(savedTransformer);
    }

    public TransformerDTO updateTransformer(Integer id, TransformerDTO dto) {

        Transformer transformer = repository.findById(id).orElse(null);

        logger.debug("Checking if Transformer exists");
        if (transformer == null) {
            logger.error("Transformer {} not found",dto.getSerialNumber());
            throw new ResourceNotFoundException("Transformer with given id : "+id+" not found");
        }

        if (!transformer.getSerialNumber().equals(dto.getSerialNumber()) && repository.existsBySerialNumber(dto.getSerialNumber())) {
            logger.error("Transformer with Serial Number {} already exists",dto.getSerialNumber());
            throw new DuplicateResourceException("Serial Number : "+ dto.getSerialNumber() +" already exists");
        }

        transformer.setSerialNumber(dto.getSerialNumber());
        transformer.setTransformerName(dto.getTransformerName());
        transformer.setTransformerType(dto.getTransformerType());
        transformer.setCapacity(dto.getCapacity());
        transformer.setLocation(dto.getLocation());
        transformer.setManufacturer(dto.getManufacturer());

        Transformer updatedTransformer = repository.save(transformer);
        logger.info("Transformer {} updated Successfully",dto.getSerialNumber());

        return convertToDTO(updatedTransformer);
    }

    public List<TransformerDTO> displayTransformers() {

        List<Transformer> transformers = repository.findAll();

        List<TransformerDTO> dtoList = new ArrayList<>();

        logger.info("Displaying every item in the Transformer database");
        for (Transformer transformer : transformers) {
            dtoList.add(convertToDTO(transformer));
        }

        return dtoList;
    }

    public TransformerDTO findById(Integer id) {

        Transformer transformer = repository.findById(id).orElse(null);

        logger.debug("Checking if Transformer with id {} exists",id);
        if (transformer == null) {
            logger.error("Transformer with id {} not found",id);
            throw new ResourceNotFoundException("Transformer with given id : "+id+" not found");
        }
        logger.info("Transformer id {} is displayed",id);
        return convertToDTO(transformer);
    }

    public String deleteTransformer(Integer id) {

        logger.debug("Checking if Transformer with id {} exists",id);
        if (!repository.existsById(id)) {
            logger.error("Transformer with id {} not found",id);
            throw new ResourceNotFoundException("Transformer with given id : "+id+" not found");
        }

        repository.deleteById(id);
        logger.info("Transformer deleted successfully");
        return "Transformer deleted successfully";
    }

}
