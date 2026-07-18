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

@Service
public class TransformerService {
    @Autowired
    private TransformerRepository repository;

    private TransformerDTO convertToDTO(Transformer transformer){
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

        if(repository.existsBySerialNumber(transformerDTO.getSerialNumber())){
            throw new DuplicateResourceException("Serial Number : "+ transformerDTO.getSerialNumber() +" already exists");
        }

        Transformer savedTransformer = repository.save(transformer);

        return convertToDTO(savedTransformer);
    }

    public TransformerDTO updateTransformer(Integer id, TransformerDTO dto) {

        Transformer transformer = repository.findById(id).orElse(null);

        if (transformer == null) {
            throw new ResourceNotFoundException("Transformer with given id : "+id+" not found");
        }

        if (!transformer.getSerialNumber().equals(dto.getSerialNumber()) && repository.existsBySerialNumber(dto.getSerialNumber())) {
            throw new DuplicateResourceException("Serial Number : "+ dto.getSerialNumber() +" already exists");
        }

        transformer.setSerialNumber(dto.getSerialNumber());
        transformer.setTransformerName(dto.getTransformerName());
        transformer.setTransformerType(dto.getTransformerType());
        transformer.setCapacity(dto.getCapacity());
        transformer.setLocation(dto.getLocation());
        transformer.setManufacturer(dto.getManufacturer());

        Transformer updatedTransformer = repository.save(transformer);

        return convertToDTO(updatedTransformer);
    }

    public List<TransformerDTO> displayTransformers() {

        List<Transformer> transformers = repository.findAll();

        List<TransformerDTO> dtoList = new ArrayList<>();

        for (Transformer transformer : transformers) {
            dtoList.add(convertToDTO(transformer));
        }

        return dtoList;
    }

    public TransformerDTO findById(Integer id) {

        Transformer transformer = repository.findById(id).orElse(null);

        if (transformer == null) {
            throw new ResourceNotFoundException("Transformer with given id : "+id+" not found");
        }

        return convertToDTO(transformer);
    }

    public String deleteTransformer(Integer id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Transformer with given id : "+id+" not found");
        }

        repository.deleteById(id);

        return "Transformer deleted successfully";
    }

}
