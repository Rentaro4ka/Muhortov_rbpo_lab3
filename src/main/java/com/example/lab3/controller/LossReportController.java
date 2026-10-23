package com.example.lab3.controller;

import com.example.lab3.model.LossReport;
import com.example.lab3.service.LossReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loss-reports")
public class LossReportController {
    private final LossReportService service;

    public LossReportController(LossReportService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LossReport>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LossReport> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LossReport> create(@RequestBody LossReport report) {
        return service.create(report)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LossReport> update(@PathVariable Long id, @RequestBody LossReport patch) {
        return service.update(id, patch).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}