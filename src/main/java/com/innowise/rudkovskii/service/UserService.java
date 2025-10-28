package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

}
