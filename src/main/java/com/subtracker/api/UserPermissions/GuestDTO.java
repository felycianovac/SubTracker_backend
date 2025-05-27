package com.subtracker.api.UserPermissions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuestDTO {
    private int guestId;
    private String guestEmail;
    private String permission; // GUEST_RO or GUEST_RW
}