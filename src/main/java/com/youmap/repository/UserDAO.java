package com.youmap.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.youmap.entity.Role;
import com.youmap.entity.User;


@Repository
public interface UserDAO extends CrudRepository<User, Long> {
    User findById(Long id);
    User findByUsername(String username);
    User findByRole(Role role);
    User findByEmail(String email);
}
