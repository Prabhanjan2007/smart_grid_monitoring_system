package com.SGMS.transformerservice.controller;

import com.SGMS.transformerservice.model.TransformerDTO;
import com.SGMS.transformerservice.service.TransformerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transformers")
public class TransformerController {

    @Autowired
    private TransformerService service;

    @PostMapping
    public ResponseEntity<TransformerDTO> addTransformer(@Valid @RequestBody TransformerDTO dto){
        TransformerDTO transformerDTO = service.addTransformer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transformerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransformerDTO> updateTransformer(@PathVariable int id ,@Valid @RequestBody TransformerDTO dto){
        TransformerDTO transformerDTO = service.updateTransformer(id,dto);
        return ResponseEntity.ok(transformerDTO);
    }

    @GetMapping
    public ResponseEntity<List<TransformerDTO>> displayTransformers() {
        List<TransformerDTO> transformers = service.displayTransformers();
        return ResponseEntity.ok(transformers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransformerDTO> findById(@PathVariable Integer id) {
        TransformerDTO transformer = service.findById(id);
        return ResponseEntity.ok(transformer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransformer(@PathVariable Integer id) {
        service.deleteTransformer(id);
        return ResponseEntity.ok("Transformer deleted successfully");
    }

}
