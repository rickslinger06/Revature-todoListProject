package com.revature.toDoList.auth;

import com.revature.toDoList.entity.User;
import com.revature.toDoList.exception.UserNotFoundFoundException;
import com.revature.toDoList.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoListUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundFoundException("User not found"));

        String role = u.getRole();
        if(role == null || role.isBlank()){
            role = "USER";
        }
        if(!role.startsWith("ROLE_")){
            role = "ROLE_" + role;
        }

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
