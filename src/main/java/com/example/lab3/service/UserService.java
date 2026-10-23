package com.example.lab3.service;

import com.example.lab3.model.User;
import com.example.lab3.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() { return repository.findAll(); }
    public Optional<User> findById(Long id) { return repository.findById(id); }

    public User create(User user) {
        user.setId(null);
        return repository.save(user);
    }

    public Optional<User> update(Long id, User patch) {
        return repository.findById(id).map(existing -> {
            if (patch.getName() != null) existing.setName(patch.getName());
            if (patch.getEmail() != null) existing.setEmail(patch.getEmail());
            if (patch.getPhone() != null) existing.setPhone(patch.getPhone());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}