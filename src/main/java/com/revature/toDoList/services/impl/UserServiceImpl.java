package com.revature.toDoList.services.impl;

import com.revature.toDoList.dto.request.RegisterRequest;
import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.dto.mapper.UserMapper;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.exception.UserExistsException;
import com.revature.toDoList.exception.UserNotFoundFoundException;
import com.revature.toDoList.repository.UserRepository;
import com.revature.toDoList.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDTO registerUser(RegisterRequest rr) {

        if(existByUsername(rr.getUsername())){
            throw new UserExistsException("User already exist");
        }
        User user = new User();
        user.setUsername(rr.getUsername());
        user.setEmail(rr.getEmail());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(rr.getPassword()));

        User savedUser = userRepository.save(user);

        return UserMapper.toDTO(savedUser);

    }

    @Override
    public UserDTO getUserById(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundFoundException("User Not Found"));
       return UserMapper.toDTO(user);

    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundFoundException("User not found"));

        return UserMapper.toDTO(user);
    }

    @Override
    public boolean existByUsername(String username) {
       boolean userNameExists = userRepository.existsByUsername(username);
        if(userNameExists){
            return true;
        }
        return false;
    }

    @Override
    public void makeUserAdmin(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundFoundException("User does not exists")
        );
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAllUsers() {

        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            throw new UsernameNotFoundException("No users found");
        }
        return users.stream().map(UserMapper::toDTO).toList();

    }


}
