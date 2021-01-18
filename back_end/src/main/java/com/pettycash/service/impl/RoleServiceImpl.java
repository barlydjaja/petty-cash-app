package com.pettycash.service.impl;

import com.pettycash.entity.Role;
import com.pettycash.entity.User;
import com.pettycash.repository.RoleRepository;
import com.pettycash.service.RoleService;
import com.pettycash.service.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.findByRoleName(name);
    }

    @Override
    public Role getById(long roleId) {
        return roleRepository.getOne(roleId);
    }

}
