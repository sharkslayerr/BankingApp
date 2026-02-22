package com.bank.onlinebanking.service;

import com.bank.onlinebanking.entity.User;
import com.bank.onlinebanking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {
    @Autowired
    private UserRepository repo;

    public User registerUser(User user) {
        return repo.save(user);
    }
    
    public User getBalance(String email) {
        return repo.findById(email).orElse(null);
    }
}