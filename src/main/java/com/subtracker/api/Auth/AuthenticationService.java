package com.subtracker.api.Auth;

import com.subtracker.api.Security.JwtService;
import com.subtracker.api.User.Role;
import com.subtracker.api.User.UserDTO;
import com.subtracker.api.User.Users;
import com.subtracker.api.User.UsersRepository;
import com.subtracker.api.UserPermissions.UserPermissions;
import com.subtracker.api.UserPermissions.UserPermissionsRepository;
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
    private final UserPermissionsRepository userPermissionsRepository;


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

    public AuthResponse logout(HttpServletResponse response) {
        ResponseCookie tokenCookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());

        return AuthResponse.builder()
                .message("Logout successful")
                .build();
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

    public ContextDTO switchContext(Users currentUser, SwitchContextRequest request, HttpServletResponse response) {
        int guestId = currentUser.getUserId();
        int ownerId = request.getOwnerId();

        UserPermissions permission = userPermissionsRepository
                .findByGuestUserIdAndOwnerUserId(guestId, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("You do not have access to this owner's data."));

        usersRepository.findById(guestId).ifPresentOrElse(
                existingUser -> {
                    existingUser.setRole(permission.getPermission());
                    usersRepository.save(existingUser);
                },
                () -> {
                    throw new IllegalArgumentException("Current user not found");
                }
        );


        Users contextUser = Users.builder()
                .userId(currentUser.getUserId())
                .email(currentUser.getEmail())
                .role(permission.getPermission())
                .build();

        String jwt = jwtService.generateToken(contextUser);
        setJwtTokenInCookie(response, jwt);

        AuthResponse authResponse = AuthResponse.builder()
                .message("Switched to context of owner with ID: " + ownerId)
                .user(UserDTO.fromEntity(contextUser))
                .build();

        return new ContextDTO(authResponse, ownerId);
    }

    public AuthResponse revertContext(Users currentUser, HttpServletResponse response) {
        Users ownerUser = Users.builder()
                .userId(currentUser.getUserId())
                .email(currentUser.getEmail())
                .role(Role.OWNER)
                .build();

        usersRepository.findById(currentUser.getUserId()).ifPresentOrElse(
                existingUser -> {
                    existingUser.setRole(Role.OWNER);
                    usersRepository.save(existingUser);
                },
                () -> {
                    throw new IllegalArgumentException("Current user not found");
                }
        );

        String jwt = jwtService.generateToken(ownerUser);
        setJwtTokenInCookie(response, jwt);

        return AuthResponse.builder()
                .message("Returned to OWNER context")
                .user(UserDTO.fromEntity(ownerUser))
                .build();
    }


    public UserDTO getCurrentUser(Users currentUser) {
        return UserDTO.fromEntity(currentUser);
    }
}
