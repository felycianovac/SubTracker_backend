package com.subtracker.api.UserPermissions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPermissionsRepository extends JpaRepository<UserPermissions, Integer> {
    Optional<UserPermissions> findByGuestUserIdAndOwnerUserId(int guestId, int ownerId);
    List<UserPermissions> findAllByGuestUserId(int guestId);
}
