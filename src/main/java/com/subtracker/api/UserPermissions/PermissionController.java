package com.subtracker.api.UserPermissions;

import com.subtracker.api.User.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionsService userPermissionsService;

    @PostMapping("/add")
    public ResponseEntity<?> addPermission(@RequestBody PermissionRequest request,
                                           @AuthenticationPrincipal Users currentUser) {
        try {
            String result = userPermissionsService.addPermission(currentUser, request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePermission(@RequestBody PermissionRequest request,
                                              @AuthenticationPrincipal Users currentUser) {
        try {
            String result = userPermissionsService.updatePermission(currentUser, request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePermission(@RequestBody String guestEmail,
                                              @AuthenticationPrincipal Users currentUser) {
        try {
            String result = userPermissionsService.removePermission(currentUser, guestEmail);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/contexts")
    public ResponseEntity<List<ContextDTO>> getContexts(@AuthenticationPrincipal Users currentUser) {
        List<ContextDTO> contexts = userPermissionsService.getAvailableContexts(currentUser);
        return ResponseEntity.ok(contexts);
    }


    @GetMapping("/guests")
    public ResponseEntity<List<GuestDTO>> getGuests(@AuthenticationPrincipal Users currentUser) {
        try {
            List<GuestDTO> guests = userPermissionsService.getGuestsWithAccess(currentUser);
            return ResponseEntity.ok(guests);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }



}
