package com.subtracker.api.UserPermissions;

import com.subtracker.api.User.Role;
import lombok.Data;

@Data
public class PermissionRequest {
    private String guestEmail;
    private Role permission; // GUEST_RW or GUEST_RO
}