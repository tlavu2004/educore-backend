package com.tlavu.educore.auth.shared.infrastructure.security;

import com.tlavu.educore.auth.user.domain.entity.User;
import com.tlavu.educore.auth.user.domain.enums.UserRole;
import com.tlavu.educore.auth.user.domain.enums.UserStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID userId;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public CustomUserDetails(
            UUID userId,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            boolean enabled
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static CustomUserDetails fromUser(User user) {
        return new CustomUserDetails(
                user.getId(),
                user.getEmail().toString(),
                user.getHashedPassword().toString(),
                mapRolesToAuthorities(user.getRole()),
                true,
                user.getStatus() != UserStatus.SUSPENDED,
                true,
                user.getStatus() == UserStatus.ACTIVATED
        );
    }

    private static Collection<? extends GrantedAuthority> mapRolesToAuthorities(UserRole role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public UUID getId() { return userId; }

    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public boolean isAccountNonExpired() { return accountNonExpired; }
    @Override public boolean isAccountNonLocked() { return accountNonLocked; }
    @Override public boolean isCredentialsNonExpired() { return credentialsNonExpired; }
    @Override public boolean isEnabled() { return enabled; }
}
