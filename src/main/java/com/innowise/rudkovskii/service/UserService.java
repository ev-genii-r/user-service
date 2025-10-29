package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user){
        userRepository.save(user);
    }

    public User getById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void updateUser(int id, User user){
        userRepository.updateUser(id,
                user.getName(),
                user.getSurname(),
                user.getBirthDate(),
                user.getEmail());
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }
}
