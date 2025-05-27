package com.subtracker.api.Auth;

import com.subtracker.api.Security.JwtService;
import com.subtracker.api.User.Role;
import com.subtracker.api.User.Users;
import com.subtracker.api.User.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


    public RegisterResponse register(RegisterRequest request, HttpServletResponse response) {
        Optional<Users> usersOptional = usersRepository.findByEmail(request.getEmail());

        if(usersOptional.isPresent()) {
            return RegisterResponse.builder()
                    .message("Email already registered")
                    .build();
        }

        System.out.println("Registering user with email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());
        Users user = Users.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        String jwtToken = jwtService.generateToken(user);
        setJwtTokenInCookie(response, jwtToken);

        usersRepository.save(user);
        return new RegisterResponse(
                "User registered successfully",
                RegisterResponse.fromEntity(user));
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
