package com.example.lab3.service;

import com.example.lab3.model.LossReport;
import com.example.lab3.repository.LossReportRepository;
import com.example.lab3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LossReportService {
    private final LossReportRepository repository;
    private final UserRepository userRepository;

    public LossReportService(LossReportRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<LossReport> findAll() { return repository.findAll(); }
    public Optional<LossReport> findById(Long id) { return repository.findById(id); }

    public Optional<LossReport> create(LossReport report) {
        if (report.getApplicantId() == null || !userRepository.existsById(report.getApplicantId())) {
            return Optional.empty();
        }
        report.setId(null);
        report.setCreatedAt(LocalDateTime.now());
        if (report.getStatus() == null) report.setStatus("OPEN");
        return Optional.of(repository.save(report));
    }

    public Optional<LossReport> update(Long id, LossReport patch) {
        return repository.findById(id).map(existing -> {
            if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
            if (patch.getCategory() != null) existing.setCategory(patch.getCategory());
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