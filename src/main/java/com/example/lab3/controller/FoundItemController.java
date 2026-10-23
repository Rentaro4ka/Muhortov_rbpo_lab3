package com.example.lab3.controller;

import com.example.lab3.model.FoundItem;
import com.example.lab3.service.FoundItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-items")
public class FoundItemController {
    private final FoundItemService service;

    public FoundItemController(FoundItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FoundItem>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FoundItem> create(@RequestBody FoundItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(item));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FoundItem> update(@PathVariable Long id, @RequestBody FoundItem patch) {
        return service.update(id, patch).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}