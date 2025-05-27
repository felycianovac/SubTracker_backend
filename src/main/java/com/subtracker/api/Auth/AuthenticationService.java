package com.subtracker.api.Auth;

import com.subtracker.api.Security.JwtService;
import com.subtracker.api.User.Role;
import com.subtracker.api.User.UserDTO;
import com.subtracker.api.User.Users;
import com.subtracker.api.User.UsersRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthResponse register(AuthRequest request, HttpServletResponse response) {
        Optional<Users> usersOptional = usersRepository.findByEmail(request.getEmail());

        if(usersOptional.isPresent()) {
            return AuthResponse.builder()
                    .message("Email already registered")
                    .build();
        }

        System.out.println("Registering user with email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());
        Users user = Users.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.OWNER)
                .build();
        String jwtToken = jwtService.generateToken(user);
        setJwtTokenInCookie(response, jwtToken);

        usersRepository.save(user);
        return new AuthResponse(
                "User registered successfully",
                UserDTO.fromEntity(user));
    }

    public AuthResponse login(AuthRequest request, HttpServletResponse response) {
        Optional<Users> usersOptional = usersRepository.findByEmail(request.getEmail());

        if(usersOptional.isEmpty()) {
            return AuthResponse.builder()
                    .message("Invalid email or password")
                    .build();
        }

        Users user = usersOptional.get();
        if(!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return AuthResponse.builder()
                    .message("Invalid email or password")
                    .build();
        }

        String jwtToken = jwtService.generateToken(user);
        setJwtTokenInCookie(response, jwtToken);

        return new AuthResponse(
                "Login successful",
                UserDTO.fromEntity(user));
    }

    private void setJwtTokenInCookie(HttpServletResponse response, String jwtToken) {
        ResponseCookie tokenCookie = ResponseCookie.from("auth_token", jwtToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
    }

}
