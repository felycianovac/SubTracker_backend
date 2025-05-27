package com.subtracker.api.UserPermissions;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContextDTO {
    private int ownerId;
    private String ownerEmail;
    private String permission; // GUEST_RW or GUEST_RO

}
