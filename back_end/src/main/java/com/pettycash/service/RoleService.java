package com.pettycash.service;

import com.pettycash.entity.Role;
public interface RoleService {
    Role getByName(String name);
    Role getById(long roleId);
}
