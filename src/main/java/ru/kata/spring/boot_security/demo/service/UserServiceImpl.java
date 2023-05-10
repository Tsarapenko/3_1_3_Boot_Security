package ru.kata.spring.boot_security.demo.service;


import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.PersistenceException;




@Service
public class UserServiceImpl implements UserService {
   private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                            @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(Long id){
        User user = null;
        Optional<User> userFromBD = userRepository.findById(id);
        if (userFromBD.isPresent()) {
            user = userFromBD.get();
        }
        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Override
    @Transactional
    public User findByUsername (String username) {
        return userRepository.findByUsername(username).get();
    }
    @Override
    @Transactional
    public boolean saveUser(User user){
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       try{
           userRepository.save(user);
       } catch (PersistenceException e){
           return false;
       }
       return true;

    }

    @Override
    @Transactional
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(User user, Long id) {
        User userFromDb = userRepository.findById(id).get();
        if (!userFromDb.getPassword().equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUsername(user.getUsername());
        userRepository.save(user);
    }

}
