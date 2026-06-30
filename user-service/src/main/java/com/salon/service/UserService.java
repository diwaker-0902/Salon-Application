package com.salon.service;

import java.util.List;
import com.salon.modal.User;

public interface UserService {

    User createUser(User user);
    User getUserById(Long id) throws Exception;
    List<User> getAllUsers();
    void deleteUser(Long id) throws Exception;
    User updateUser(Long id, User user) throws Exception;

    
}
