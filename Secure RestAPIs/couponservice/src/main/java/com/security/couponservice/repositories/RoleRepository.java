package com.security.couponservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.security.couponservice.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
