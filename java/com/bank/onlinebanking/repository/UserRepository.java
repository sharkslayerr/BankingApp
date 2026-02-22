package com.bank.onlinebanking.repository;

import com.bank.onlinebanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}