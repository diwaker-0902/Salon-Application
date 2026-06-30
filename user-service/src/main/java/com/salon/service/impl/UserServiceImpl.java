package com.salon.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.salon.exception.UserException;
import com.salon.modal.User;
import com.salon.repository.UserRepository;
import com.salon.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws UserException {
        Optional<User> opt = userRepository.findById(id); // provided optional means either the user is present or not present
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new UserException("User not found");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) throws UserException {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            throw new UserException("User not exist with id " + id);
        }
        userRepository.deleteById(opt.get().getId());
    }

    @Override
    public User updateUser(Long id, User user) throws UserException {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            throw new UserException("User not found with id " + id);
        }

        User existingUser = opt.get();

        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setPhone(user.getPhone());
        existingUser.setUsername(user.getUsername());

        
        return userRepository.save(existingUser);
    }


}
