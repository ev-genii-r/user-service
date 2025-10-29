package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
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

    public User createUser(User user) throws ValidationException {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ValidationException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User getById(int id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User updateUser(int id, User user){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return userRepository.updateUser(id,
                user.getName(),
                user.getSurname(),
                user.getBirthDate(),
                user.getEmail());
    }

    public void deleteUser(int id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
