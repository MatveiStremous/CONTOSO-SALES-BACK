package com.example.contoso.repository;

import com.example.contoso.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:12 PM
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByEmail(String email);
}
