package com.subtracker.api.User;

import com.subtracker.api.User.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int id;
    private String email;
    private String role;

    public static UserDTO fromEntity(Users user){
        return UserDTO.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().name().toString())
                .build();
    }

}
