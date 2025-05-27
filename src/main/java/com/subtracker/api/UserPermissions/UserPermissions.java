package com.subtracker.api.UserPermissions;

import com.subtracker.api.User.Role;
import com.subtracker.api.User.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_permissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"guest_id", "owner_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Users guest;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role permission;
}
