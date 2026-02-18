package com.revature.toDoList.controller;

import com.revature.toDoList.auth.JwtService;
import com.revature.toDoList.dto.*;
import com.revature.toDoList.dto.mapper.UserMapper;
import com.revature.toDoList.dto.request.AuthRequest;
import com.revature.toDoList.dto.request.RegisterRequest;
import com.revature.toDoList.dto.response.AuthResponse;
import com.revature.toDoList.services.UserService;
import com.revature.toDoList.util.SecurityUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private static final String REFRESH_COOKIE = "refreshToken";


    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest) {

        UserDTO createdUser = userService.registerUser(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest req, HttpServletResponse response){

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        UserDetails principal = (UserDetails) auth.getPrincipal();

        UserDTO dto = userService.getUserByUsername(principal.getUsername());

        String accessToken = jwtService.generateAccessToken(UserMapper.toEntity(dto));
        String refreshToken = jwtService.generateRefreshToken(UserMapper.toEntity(dto));

        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_COOKIE,refreshToken)
                        .httpOnly(true)
                        .secure(false) // set true in production HTTPS
                        .path("/api/v1/auth")
                        .sameSite("None")
                        .maxAge(Duration.ofDays(15))
                        .build();

        response.addHeader(HttpHeaders.SET_COOKIE,refreshCookie.toString());

        log.info("username={} Successfully Authenticated", jwtService.extractUsername(accessToken));
        return ResponseEntity.ok(new AuthResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request,
                                                HttpServletResponse response) {

        String refreshToken = readCookie(request, REFRESH_COOKIE);
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        jwtService.assertTokenType(refreshToken, "refresh");

        String username = jwtService.extractUsername(refreshToken);
        UserDTO dto = userService.getUserByUsername(username);

        //user gets another 15 minute token (set on the config)
        String newAccessToken = jwtService.generateAccessToken(UserMapper.toEntity(dto));

        // Optional rotation: issue a new refresh cookie each refresh
        String newRefreshToken = jwtService.generateRefreshToken(UserMapper.toEntity(dto));
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_COOKIE, newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth")
                .sameSite("None") //set to Lax if have same domain client and server side
                .maxAge(Duration.ofDays(15))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        //clearing the cookie
        ResponseCookie deleteCookie = ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth")
                .sameSite("None")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return ResponseEntity.noContent().build();
    }

    private String readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }


}
