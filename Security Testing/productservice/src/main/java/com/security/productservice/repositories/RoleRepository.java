package com.security.productservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.security.productservice.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
