package com.example.contoso.repository;

import com.example.contoso.entity.Request;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:17 PM
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByUserId(Integer id);
}
