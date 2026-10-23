package com.example.lab3.service;

import com.example.lab3.model.FoundItem;
import com.example.lab3.model.ItemClaim;
import com.example.lab3.model.LossReport;
import com.example.lab3.repository.FoundItemRepository;
import com.example.lab3.repository.ItemClaimRepository;
import com.example.lab3.repository.LossReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ItemClaimService {
    private final ItemClaimRepository repository;
    private final LossReportRepository lossReportRepository;
    private final FoundItemRepository foundItemRepository;

    public ItemClaimService(ItemClaimRepository repository,
                            LossReportRepository lossReportRepository,
                            FoundItemRepository foundItemRepository) {
        this.repository = repository;
        this.lossReportRepository = lossReportRepository;
        this.foundItemRepository = foundItemRepository;
    }

    public List<ItemClaim> findAll() { return repository.findAll(); }
    public Optional<ItemClaim> findById(Long id) { return repository.findById(id); }


    @Transactional
    public Optional<ItemClaim> createClaim(ItemClaim claim) {
        Optional<LossReport> report = lossReportRepository.findById(claim.getLossReportId());
        if (report.isEmpty() || !"OPEN".equals(report.get().getStatus())) return Optional.empty();

        Optional<FoundItem> item = foundItemRepository.findById(claim.getFoundItemId());
        if (item.isEmpty() || !"STORED".equals(item.get().getStatus())) return Optional.empty();

        claim.setId(null);
        claim.setStatus("PENDING");
        claim.setCreatedAt(LocalDateTime.now());
        return Optional.of(repository.save(claim));
    }


    @Transactional
    public Optional<ItemClaim> approveClaim(Long id) {
        return repository.findById(id).map(claim -> {
            if (!"PENDING".equals(claim.getStatus())) throw new IllegalStateException("Claim not pending");

            claim.setStatus("APPROVED");
            repository.save(claim);

            foundItemRepository.findById(claim.getFoundItemId()).ifPresent(item -> {
                item.setStatus("RESERVED");
                foundItemRepository.save(item);
            });

            lossReportRepository.findById(claim.getLossReportId()).ifPresent(report -> {
                report.setStatus("CLOSED");
                lossReportRepository.save(report);
            });

            return claim;
        });
    }


    @Transactional
    public Optional<ItemClaim> rejectClaim(Long id) {
        return repository.findById(id).map(claim -> {
            if (!"PENDING".equals(claim.getStatus())) throw new IllegalStateException("Claim not pending");

            claim.setStatus("REJECTED");
            repository.save(claim);


            boolean hasOtherClaims = repository.findAll().stream()
                    .anyMatch(c -> c.getFoundItemId().equals(claim.getFoundItemId())
                            && !c.getId().equals(claim.getId())
                            && "PENDING".equals(c.getStatus()));

            if (!hasOtherClaims) {
                foundItemRepository.findById(claim.getFoundItemId()).ifPresent(item -> {
                    item.setStatus("STORED");
                    foundItemRepository.save(item);
                });
            }
            return claim;
        });
    }


    public Optional<ItemClaim> update(Long id, ItemClaim patch) {
        return repository.findById(id).map(existing -> {
            if (patch.getEvidence() != null) existing.setEvidence(patch.getEvidence());
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