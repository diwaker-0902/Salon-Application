package com.salon.repository;

import com.salon.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    // this repository is for the User entity/table and unique identifier type is Long


}