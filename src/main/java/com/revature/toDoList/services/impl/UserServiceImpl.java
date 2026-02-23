package com.revature.toDoList.services.impl;

import com.revature.toDoList.dto.request.RegisterRequest;
import com.revature.toDoList.dto.UserDTO;
import com.revature.toDoList.dto.mapper.UserMapper;
import com.revature.toDoList.dto.request.ResetPasswordRequest;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.exception.InvalidPasswordException;
import com.revature.toDoList.exception.UserExistsException;
import com.revature.toDoList.exception.UserNotFoundFoundException;
import com.revature.toDoList.repository.UserRepository;
import com.revature.toDoList.services.UserService;
import com.revature.toDoList.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
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
    public void changeUseRole(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundFoundException("User does not exists")
        );
        if(user.getRole().equals("USER")){
            user.setRole("ADMIN");
        }else{
            user.setRole("USER");
        }
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> findAllUsers(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        if (users.isEmpty()) {
            throw new UsernameNotFoundException("No users found");
        }
        return users.map(UserMapper::toDTO);
    }

    @Override
    public void resetPassword(ResetPasswordRequest passwordRequest) {

        String username = SecurityUtils.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundFoundException("No user found"));



        // Validate current password
      String encodedPassRequest = passwordEncoder.encode(passwordRequest.getCurrentPassword());
      if(encodedPassRequest.equals(user.getPassword())){
          throw new InvalidPasswordException("Password do not match");
      }

        // Optional: prevent same password
        if (passwordEncoder.matches(passwordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidPasswordException("New password must be different from current password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);
        log.info("Password reset successful: {}", username);
    }

}
