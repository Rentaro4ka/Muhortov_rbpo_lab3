package com.example.lab3.service;

import com.example.lab3.model.FoundItem;
import com.example.lab3.model.HandoverRecord;
import com.example.lab3.model.ItemClaim;
import com.example.lab3.repository.FoundItemRepository;
import com.example.lab3.repository.HandoverRecordRepository;
import com.example.lab3.repository.ItemClaimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HandoverRecordService {
    private final HandoverRecordRepository repository;
    private final ItemClaimRepository itemClaimRepository;
    private final FoundItemRepository foundItemRepository;

    public HandoverRecordService(HandoverRecordRepository repository,
                                 ItemClaimRepository itemClaimRepository,
                                 FoundItemRepository foundItemRepository) {
        this.repository = repository;
        this.itemClaimRepository = itemClaimRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public List<HandoverRecord> findAll() { return repository.findAll(); }
    public Optional<HandoverRecord> findById(Long id) { return repository.findById(id); }


    @Transactional
    public Optional<HandoverRecord> createRecord(HandoverRecord record) {
        Optional<ItemClaim> claim = itemClaimRepository.findById(record.getClaimId());
        if (claim.isEmpty() || !"APPROVED".equals(claim.get().getStatus())) return Optional.empty();

        record.setId(null);
        record.setHandoverDate(LocalDateTime.now());
        record.setConfirmed(false);
        return Optional.of(repository.save(record));
    }


    @Transactional
    public Optional<HandoverRecord> confirmRecord(Long id) {
        return repository.findById(id).map(record -> {
            if (Boolean.TRUE.equals(record.getConfirmed())) throw new IllegalStateException("Already confirmed");

            record.setConfirmed(true);
            repository.save(record);

            itemClaimRepository.findById(record.getClaimId()).ifPresent(claim -> {
                claim.setStatus("COMPLETED");
                itemClaimRepository.save(claim);

                foundItemRepository.findById(claim.getFoundItemId()).ifPresent(item -> {
                    item.setStatus("RETURNED");
                    foundItemRepository.save(item);
                });
            });

            return record;
        });
    }


    public Optional<HandoverRecord> update(Long id, HandoverRecord patch) {
        return repository.findById(id).map(existing -> {
            if (patch.getConfirmed() != null) existing.setConfirmed(patch.getConfirmed());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}