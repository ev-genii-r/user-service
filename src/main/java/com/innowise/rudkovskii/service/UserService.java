package com.innowise.rudkovskii.service;

import com.innowise.rudkovskii.entity.User;
import com.innowise.rudkovskii.exception.ResourceNotFoundException;
import com.innowise.rudkovskii.exception.ValidationException;
import com.innowise.rudkovskii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @CachePut(value = "users", key = "#result.id")
    @Transactional
    public User createUser(User user) throws ValidationException {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ValidationException("Email already exists");
        }
        User newUser = new User();

        newUser.setName(user.getName());
        newUser.setSurname(user.getSurname());
        newUser.setBirthDate(user.getBirthDate());
        newUser.setEmail(user.getEmail());
        newUser.setCards(user.getCards());

        return userRepository.save(newUser);
    }

    @Cacheable(value = "users", key = "#id")
    public User getById(int id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id));
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }


    @Cacheable(value = "usersByEmail", key = "#email")
    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email: " + email));
    }

    @CachePut(value = "users", key = "#id")
    @Transactional
    public User updateUser(int id, User user){

        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User with id: " + id);
        }

        if(userRepository.existsByEmail(user.getEmail())){
            throw new ValidationException("Email already exists");
        }

        return userRepository.updateUser(id,
                user.getName(),
                user.getSurname(),
                user.getBirthDate(),
                user.getEmail());
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void deleteUser(int id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
