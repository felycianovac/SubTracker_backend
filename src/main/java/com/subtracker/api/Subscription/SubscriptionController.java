package com.subtracker.api.Subscription;

import com.subtracker.api.User.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<Subscription>> getAll(@AuthenticationPrincipal Users user,
                                                     @RequestParam int contextUserId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptions(user, contextUserId));
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@AuthenticationPrincipal Users user,
                                               @RequestParam int contextUserId,
                                               @RequestBody Subscription sub) {
        return ResponseEntity.ok(subscriptionService.create(user, contextUserId, sub));
    }

    @PutMapping
    public ResponseEntity<Subscription> update(@AuthenticationPrincipal Users user,
                                               @RequestParam int contextUserId,
                                               @RequestBody Subscription sub) {
        return ResponseEntity.ok(subscriptionService.update(user, contextUserId, sub));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal Users user,
                                    @RequestParam int contextUserId,
                                    @PathVariable Long id) {
        subscriptionService.delete(user, contextUserId, id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getById(@AuthenticationPrincipal Users user,
                                                @RequestParam int contextUserId,
                                                @PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getById(user, contextUserId, id));
    }
}
