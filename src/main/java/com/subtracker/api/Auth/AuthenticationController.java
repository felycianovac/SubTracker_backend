package com.subtracker.api.Auth;

import com.subtracker.api.User.UserDTO;
import com.subtracker.api.User.Users;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody AuthRequest request,
                                 HttpServletResponse response) {
        return authenticationService.register(request, response);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request,
                              HttpServletResponse response) {
        return authenticationService.login(request, response);
    }

    @PostMapping("/logout")
    public AuthResponse logout(HttpServletResponse response) {
        return authenticationService.logout(response);
    }

    @PostMapping("/switch-context")
    public ContextDTO switchContext(@AuthenticationPrincipal Users currentUser,
                                      @RequestBody SwitchContextRequest request,
                                      HttpServletResponse response) {
        return authenticationService.switchContext(currentUser, request, response);
    }
    @PostMapping("/revert-context")
    public AuthResponse revertContext(@AuthenticationPrincipal Users currentUser,
                                      HttpServletResponse response) {
        return authenticationService.revertContext(currentUser, response);
    }

    @GetMapping("/current")
    public UserDTO getCurrentUser(@AuthenticationPrincipal Users currentUser) {
        return authenticationService.getCurrentUser(currentUser);
    }


}
