package com.example.lab3.controller;

import com.example.lab3.model.ItemClaim;
import com.example.lab3.service.ItemClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item-claims")
public class ItemClaimController {
    private final ItemClaimService service;

    public ItemClaimController(ItemClaimService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ItemClaim>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemClaim> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<ItemClaim> create(@RequestBody ItemClaim claim) {
        return service.createClaim(claim)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElse(ResponseEntity.badRequest().build());
    }


    @PostMapping("/create")
    public ResponseEntity<ItemClaim> createBusinessClaim(@RequestBody ItemClaim claim) {
        return service.createClaim(claim)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .orElse(ResponseEntity.badRequest().build());
    }


    @PatchMapping("/{id}/approve")
    public ResponseEntity<ItemClaim> approveClaim(@PathVariable Long id) {
        try {
            return service.approveClaim(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PatchMapping("/{id}/reject")
    public ResponseEntity<ItemClaim> rejectClaim(@PathVariable Long id) {
        try {
            return service.rejectClaim(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemClaim> update(@PathVariable Long id, @RequestBody ItemClaim patch) {
        return service.update(id, patch).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}