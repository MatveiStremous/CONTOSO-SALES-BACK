package com.example.contoso.repository;

import com.example.contoso.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:23 AM
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
