package User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String passwordHash;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name().toUpperCase()));
    }


    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
