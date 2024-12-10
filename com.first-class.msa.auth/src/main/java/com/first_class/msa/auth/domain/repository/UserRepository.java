package com.first_class.msa.auth.domain.repository;

import com.first_class.msa.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByAccount(String account);
}
