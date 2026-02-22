package com.bank.onlinebanking.controller;

import com.bank.onlinebanking.entity.User;
import com.bank.onlinebanking.repository.UserRepository;
import com.bank.onlinebanking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private UserRepository repo;
    
    @Autowired
    private TransactionRepository transRepo;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return repo.findAll();
    } 

    @PostMapping("/register")
    public String registerUser(User user) { 
        repo.save(user);
        
        return "User Registered Successfully with Email: " + user.getEmail();
    }

    @GetMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password) {
        User user = repo.findById(email).orElse(null);
        if (user != null && user.getPassword().equals(password)) {
            return "Login Success! Welcome " + user.getName();
        }
        return "Login Failed! Invalid credentials";
    }

    @GetMapping("/balance")
    public String getBalance(@RequestParam String email) {
        User user = repo.findById(email).orElse(null);
        if (user != null) {
            return "Current Balance for " + user.getName() + " is: " + user.getBalance();
        }
        return "User not found!";
    }

    @GetMapping("/transfer")
    public String transferMoney(@RequestParam String fromEmail, 
                                @RequestParam String toEmail, 
                                @RequestParam double amount) {
        
        User sender = repo.findById(fromEmail).orElse(null);
        User receiver = repo.findById(toEmail).orElse(null);

        if (sender == null || receiver == null) {
            return "Error: Invalid Sender or Receiver email!";
        }

        if (sender.getBalance() < amount) {
            return "Error: Insufficient Balance!";
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        repo.save(sender);
        repo.save(receiver);

        com.bank.onlinebanking.entity.Transaction t = new com.bank.onlinebanking.entity.Transaction();
        t.setSenderEmail(fromEmail);
        t.setReceiverEmail(toEmail);
        t.setAmount(amount);
        t.setTimestamp(java.time.LocalDateTime.now());
        
        transRepo.save(t);

        return "Success: Amount " + amount + " transferred from " + sender.getName() + " to " + receiver.getName();
    }

    @GetMapping("/statement")
    public List<com.bank.onlinebanking.entity.Transaction> getStatement(@RequestParam String email) {
        return transRepo.findBySenderEmailOrReceiverEmail(email, email);
    }
}