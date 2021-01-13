package com.pettycash.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pettycash.dto.UserDTO;
import com.pettycash.entity.User;
import com.pettycash.repository.UserRepository;
import com.pettycash.service.UserService;

import javassist.NotFoundException;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository repo;

	public User addUser(UserDTO details) {
		User newUser = new User();
		newUser.setStartBalance(details.getBalance());
		newUser.setCode(details.getCode());
		newUser.setDepartment(details.getDepartment());
		newUser.setName(details.getName());
		newUser.setAccountBalance(details.getBalance());
		
		return repo.save(newUser);
	}

	public User getUserById(long userId) throws NotFoundException{
		if(repo.getOne(userId) == null) {
			throw new NotFoundException("user not found");
		}
		
		return repo.getOne(userId);
	}

	public void updateUserBalance(long userId, long amount) {
		User updateUser = repo.getOne(userId);
		updateUser.setAccountBalance(updateUser.getAccountBalance() + amount);
		repo.save(updateUser);
	}

	public void updateUser(User user, long amount) {
		user.setAccountBalance(amount);
		repo.save(user);
	}
}
