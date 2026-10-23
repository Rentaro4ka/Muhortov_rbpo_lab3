package com.example.lab3.controller;

import com.example.lab3.model.HandoverRecord;
import com.example.lab3.service.HandoverRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/handover-records")
public class HandoverRecordController {
    private final HandoverRecordService service;

    public HandoverRecordController(HandoverRecordService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<HandoverRecord>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HandoverRecord> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/create")
    public ResponseEntity<HandoverRecord> createRecord(@RequestBody HandoverRecord record) {
        return service.createRecord(record)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }


    @PatchMapping("/{id}/confirm")
    public ResponseEntity<HandoverRecord> confirmRecord(@PathVariable Long id) {
        try {
            return service.confirmRecord(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HandoverRecord> update(@PathVariable Long id, @RequestBody HandoverRecord patch) {
        return service.update(id, patch).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}