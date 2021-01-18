package com.pettycash.service;

import com.pettycash.entity.Role;
import javassist.NotFoundException;

import java.util.Set;

public interface RoleService {
    Role getByName(String name);
    Role getById(long roleId);
}
