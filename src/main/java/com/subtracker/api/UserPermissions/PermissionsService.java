package com.subtracker.api.UserPermissions;

import com.subtracker.api.User.Role;
import com.subtracker.api.User.Users;
import com.subtracker.api.User.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionsService {

    private final UsersRepository usersRepository;
    private final UserPermissionsRepository userPermissionsRepository;


    public String addPermission(Users currentUser, PermissionRequest request) {
        if (currentUser.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Only OWNERs can grant guest access.");
        }

        Optional<Users> guestOpt = usersRepository.findByEmail(request.getGuestEmail());
        if (guestOpt.isEmpty()) {
            throw new IllegalArgumentException("Guest user not found.");
        }

        Users guest = guestOpt.get();

        if (guest.getUserId() == currentUser.getUserId()) {
            throw new IllegalArgumentException("Cannot add yourself as guest.");
        }

        Optional<UserPermissions> existing = userPermissionsRepository
                .findByGuestUserIdAndOwnerUserId(guest.getUserId(), currentUser.getUserId());

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Permission already exists.");
        }

        UserPermissions permission = UserPermissions.builder()
                .guest(guest)
                .owner(currentUser)
                .permission(request.getPermission())
                .build();

        userPermissionsRepository.save(permission);
        return "Guest permission granted successfully.";
    }

    public String updatePermission(Users currentUser, PermissionRequest request) {
        if (currentUser.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Only OWNERs can update permissions.");
        }
        Optional<Users> guestOpt = usersRepository.findByEmail(request.getGuestEmail());
        if (guestOpt.isEmpty()) {
            throw new IllegalArgumentException("Guest user not found.");
        }
        Users guest = guestOpt.get();

        Optional<UserPermissions> permissionOpt = userPermissionsRepository
                .findByGuestUserIdAndOwnerUserId(guest.getUserId(), currentUser.getUserId());

        if (permissionOpt.isEmpty()) {
            return addPermission(currentUser, request);
        }

        UserPermissions permission = permissionOpt.get();

        permission.setPermission(request.getPermission());
        userPermissionsRepository.save(permission);

        return "Guest permission updated successfully.";
    }

    public String removePermission(Users currentUser, String guestEmail) {
        if (currentUser.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Only OWNERs can remove guest access.");
        }

        Optional<Users> guestOpt = usersRepository.findByEmail(guestEmail);
        if (guestOpt.isEmpty()) {
            throw new IllegalArgumentException("Guest user not found.");
        }

        Users guest = guestOpt.get();

        Optional<UserPermissions> permissionOpt = userPermissionsRepository
                .findByGuestUserIdAndOwnerUserId(guest.getUserId(), currentUser.getUserId());

        if (permissionOpt.isEmpty()) {
            throw new IllegalArgumentException("No permission found for this guest.");
        }

        userPermissionsRepository.delete(permissionOpt.get());
        return "Guest permission removed successfully.";

    }

    public List<ContextDTO> getAvailableContexts(Users currentUser) {
        return userPermissionsRepository.findAllByGuestUserId(currentUser.getUserId())
                .stream()
                .map(permission -> new ContextDTO(
                        permission.getOwner().getUserId(),
                        permission.getOwner().getEmail(),
                        permission.getPermission().name()
                ))
                .toList();
    }

    public List<GuestDTO> getGuestsWithAccess(Users currentUser) {
        if (currentUser.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("Only OWNERs can view guest access.");
        }

        return userPermissionsRepository.findAll()
                .stream()
                .filter(perm -> perm.getOwner().getUserId() == currentUser.getUserId())
                .map(perm -> new GuestDTO(
                        perm.getGuest().getUserId(),
                        perm.getGuest().getEmail(),
                        perm.getPermission().name()
                ))
                .toList();
    }



}
