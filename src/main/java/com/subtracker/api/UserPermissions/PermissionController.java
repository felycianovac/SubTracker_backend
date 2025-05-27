package com.subtracker.api.UserPermissions;

import com.subtracker.api.User.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
