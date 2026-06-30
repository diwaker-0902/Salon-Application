package com.salon.controller;

// import com.salon.repository.UserRepository;
import com.salon.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// import com.salon.exception.UserException;
import com.salon.modal.User;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // public UserController(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }

    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) { // used @RequestBody because this user will come from the client side
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); 
    }

    // @GetMapping("/api/users")
    // public User getUser() {
    //     User user = new User();

    //     user.setEmail("thewalker@gmail.com");
    //     user.setFullName("The Walker");
    //     user.setPhone("+91 7878787890");
    //     user.setRole("Customer");

    //     return user;
    // }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
        
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long id) throws Exception { // id will be accessed using the @PathVariable
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);   
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) throws Exception {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }

    @DeleteMapping("api/users/{id}")
    public ResponseEntity<String>  deleteUserById(@PathVariable Long id) throws Exception {
        userService.deleteUser(id);
        return new ResponseEntity<>("User Deleted Successfully", HttpStatus.ACCEPTED);
    }
}
