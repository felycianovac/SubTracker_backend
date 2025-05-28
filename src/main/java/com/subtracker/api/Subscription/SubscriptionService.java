package com.subtracker.api.Subscription;

import com.subtracker.api.User.Role;
import com.subtracker.api.User.Users;
import com.subtracker.api.UserPermissions.UserPermissions;
import com.subtracker.api.UserPermissions.UserPermissionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserPermissionsRepository permissionsRepository;

    public Users resolveContextUser(Users currentUser, int contextUserId) {
        if (currentUser.getUserId() == contextUserId) return currentUser;

        UserPermissions permission = permissionsRepository
                .findByGuestUserIdAndOwnerUserId(currentUser.getUserId(), contextUserId)
                .orElseThrow(() -> new SecurityException("No access to this context"));

        if (permission.getPermission() == Role.GUEST_RO || permission.getPermission() == Role.GUEST_RW) {
            return permission.getOwner();
        }

        throw new SecurityException("Insufficient permission for this context");
    }

    public List<SubscriptionDTO> getSubscriptions(Users currentUser, int contextUserId) {
        Users contextUser = resolveContextUser(currentUser, contextUserId);
        return subscriptionRepository.findAllByUsers(contextUser)
                .stream()
                .map(SubscriptionDTO::fromEntity)
                .toList();
    }

    public SubscriptionDTO create(Users currentUser, int contextUserId, Subscription sub) {
        Users contextUser = resolveContextUser(currentUser, contextUserId);

        if (currentUser.getUserId() != contextUserId &&
                !hasWritePermission(currentUser, contextUserId)) {
            throw new SecurityException("Write access denied");
        }

        sub.setUsers(contextUser);
        subscriptionRepository.save(sub);
        return SubscriptionDTO.fromEntity(sub);
    }

    public SubscriptionDTO update(Users currentUser, int contextUserId, Subscription updated) {
        Subscription existing = subscriptionRepository.findById(updated.getId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        if (existing.getUsers().getUserId() != contextUserId) {
            throw new SecurityException("Subscription does not belong to specified context");
        }

        if (currentUser.getUserId() != contextUserId &&
                !hasWritePermission(currentUser, contextUserId)) {
            throw new SecurityException("Write access denied");
        }

        updated.setUsers(existing.getUsers());
        subscriptionRepository.save(updated);
        return SubscriptionDTO.fromEntity(updated);
    }

    public void delete(Users currentUser, int contextUserId, Long id) {
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));

        if (existing.getUsers().getUserId() != contextUserId) {
            throw new SecurityException("Subscription does not belong to specified context");
        }

        if (currentUser.getUserId() != contextUserId &&
                !hasWritePermission(currentUser, contextUserId)) {
            throw new SecurityException("Write access denied");
        }

        subscriptionRepository.delete(existing);
    }

    private boolean hasWritePermission(Users guest, int ownerId) {
        return permissionsRepository.findByGuestUserIdAndOwnerUserId(guest.getUserId(), ownerId)
                .map(p -> p.getPermission() == Role.GUEST_RW)
                .orElse(false);
    }

    public SubscriptionDTO getById(Users currentUser, int contextUserId, Long id) {
        Users contextUser = resolveContextUser(currentUser, contextUserId);
        if (currentUser.getUserId() != contextUserId &&
                !hasReadPermission(currentUser, contextUserId)) {
            throw new SecurityException("Read access denied");
        }
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        if (subscription.getUsers().getUserId() != contextUserId) {
            throw new SecurityException("Subscription does not belong to specified context");
        }

        return SubscriptionDTO.fromEntity(subscription);
    }


    private boolean hasReadPermission(Users guest, int ownerId) {
        return permissionsRepository.findByGuestUserIdAndOwnerUserId(guest.getUserId(), ownerId)
                .map(p -> p.getPermission() == Role.GUEST_RO || p.getPermission() == Role.GUEST_RW)
                .orElse(false);
    }
}
