package com.example.lab3.service;

import com.example.lab3.model.FoundItem;
import com.example.lab3.repository.FoundItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FoundItemService {
    private final FoundItemRepository repository;

    public FoundItemService(FoundItemRepository repository) {
        this.repository = repository;
    }

    public List<FoundItem> findAll() { return repository.findAll(); }
    public Optional<FoundItem> findById(Long id) { return repository.findById(id); }

    public FoundItem create(FoundItem item) {
        item.setId(null);
        item.setFoundAt(LocalDateTime.now());
        if (item.getStatus() == null) item.setStatus("STORED");
        return repository.save(item);
    }

    public Optional<FoundItem> update(Long id, FoundItem patch) {
        return repository.findById(id).map(existing -> {
            if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
            if (patch.getCategory() != null) existing.setCategory(patch.getCategory());
            if (patch.getLocationFound() != null) existing.setLocationFound(patch.getLocationFound());
            if (patch.getStatus() != null) existing.setStatus(patch.getStatus());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}