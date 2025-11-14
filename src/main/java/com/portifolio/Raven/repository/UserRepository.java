package com.portifolio.Raven.repository;

import com.portifolio.Raven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {

    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);


}
