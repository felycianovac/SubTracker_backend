package com.subtracker.api.Subscription;

import com.subtracker.api.User.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@CrossOrigin
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping

    public ResponseEntity<Page<SubscriptionDTO>> getAll(@AuthenticationPrincipal Users user,
                                                        @RequestParam int contextUserId,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subscriptionService.getSubscriptions(user, contextUserId, page, size));
    }

    @PreAuthorize("hasAnyAuthority('OWNER', 'GUEST_RW')")

    @PostMapping
    public ResponseEntity<SubscriptionDTO> create(@AuthenticationPrincipal Users user,
                                                  @RequestParam int contextUserId,
                                                  @RequestBody Subscription sub) {
        return ResponseEntity.ok(subscriptionService.create(user, contextUserId, sub));
    }

    @PreAuthorize("hasAnyAuthority('OWNER', 'GUEST_RW')")
    @PutMapping
    public ResponseEntity<SubscriptionDTO> update(@AuthenticationPrincipal Users user,
                                                  @RequestParam int contextUserId,
                                                  @RequestBody Subscription sub) {
        return ResponseEntity.ok(subscriptionService.update(user, contextUserId, sub));
    }

    @PreAuthorize("hasAnyAuthority('OWNER', 'GUEST_RW')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal Users user,
                                    @RequestParam int contextUserId,
                                    @PathVariable Long id) {
        subscriptionService.delete(user, contextUserId, id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDTO> getById(@AuthenticationPrincipal Users user,
                                                   @RequestParam int contextUserId,
                                                   @PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getById(user, contextUserId, id));
    }
}
