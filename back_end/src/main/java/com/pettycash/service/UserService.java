package com.pettycash.service;

import com.pettycash.dto.UserDTO;
import com.pettycash.entity.User;

import javassist.NotFoundException;

public interface UserService {
	User addUser(UserDTO details);
	User getUserById(long userId) throws NotFoundException;
	public void updateUserBalance(long userId, long amount);
}
