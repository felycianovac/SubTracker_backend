package com.subtracker.api.Auth;

import com.subtracker.api.User.UserDTO;
import com.subtracker.api.User.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String message;
    private UserDTO user;

    public static UserDTO fromEntity(Users user){
        return UserDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().name().toString())
                .build();
    }
}

