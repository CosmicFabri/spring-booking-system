package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.Role;
import com.spring.spring_booking_system.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Long getId(Role role) {
        return role.getId();
    }
}
