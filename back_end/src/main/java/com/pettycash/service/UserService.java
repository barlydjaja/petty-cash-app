package com.pettycash.service;

import com.pettycash.dto.UserDTO;
import com.pettycash.entity.User;

import javassist.NotFoundException;

public interface UserService {
    User addUser(UserDTO details) throws Exception;

    User getUserById(long userId) throws NotFoundException;

    void updateUserBalance(long userId, long amount);

    void updateUser(User user, long amount);

    User getUserByUsername(String username);

    User changePassword(long userId, String password);
}
