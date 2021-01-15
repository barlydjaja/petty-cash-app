package com.pettycash.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pettycash.dto.UserDTO;
import com.pettycash.entity.User;
import com.pettycash.repository.UserRepository;
import com.pettycash.service.UserService;

import javassist.NotFoundException;
import sun.security.util.Password;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.repo = repo;
    }

    public User addUser(UserDTO details) throws Exception {
        User newUser = new User();
        if (repo.findByUsername(details.getName()) == null) {

            newUser.setStartBalance(details.getBalance());
            newUser.setCode(details.getCode());
            newUser.setDepartment(details.getDepartment());
            newUser.setUsername(details.getName());
            newUser.setAccountBalance(details.getBalance());
            newUser.setRole(details.getRole());
            repo.save(newUser);
        } else throw new Exception("user with the same username exists");

        return newUser;
    }

    public User getUserById(long userId) throws NotFoundException {
        if (repo.getOne(userId).getUserId() <= 0) {
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

    @Override
    public User getUserByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public User changePassword(long userId, String password) {
        User user = repo.getOne(userId);
        user.setPassword(passwordEncoder.encode(password));

        return repo.save(user);
    }
}
