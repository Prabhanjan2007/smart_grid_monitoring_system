package com.SGMS.transformerservice.controller;

import com.SGMS.transformerservice.model.TransformerDTO;
import com.SGMS.transformerservice.service.TransformerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/transformers")
public class TransformerController {

    @Autowired
    private TransformerService service;

    private static final Logger logger = LoggerFactory.getLogger(TransformerController.class);

    @PostMapping
    public ResponseEntity<TransformerDTO> addTransformer(@Valid @RequestBody TransformerDTO dto){
        logger.info("Recieved request to add Transformer");
        TransformerDTO transformerDTO = service.addTransformer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transformerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransformerDTO> updateTransformer(@PathVariable int id ,@Valid @RequestBody TransformerDTO dto){
        logger.info("Update request received successfully");
        TransformerDTO transformerDTO = service.updateTransformer(id,dto);
        return ResponseEntity.ok(transformerDTO);
    }

    @GetMapping
    public ResponseEntity<List<TransformerDTO>> displayTransformers() {
        logger.info("Request received for displaying Transformer elements");
        List<TransformerDTO> transformers = service.displayTransformers();
        return ResponseEntity.ok(transformers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransformerDTO> findById(@PathVariable Integer id) {
        logger.info("Request to find transformer by ID is received");
        TransformerDTO transformer = service.findById(id);
        return ResponseEntity.ok(transformer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransformer(@PathVariable Integer id) {
        logger.info("Delete request received successfully for Transforemer {}",id);
        service.deleteTransformer(id);
        return ResponseEntity.ok("Transformer deleted successfully");
    }

}
