package com.books.bkb.Config.Security.UserAuth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthDetail implements UserDetails {
    private final Integer id;
    private final String name;
    private final String username;
    private final String password;
    private final String email;

    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isEnabled;

    public AuthDetail(Integer id, String name, String username, String password, String email, Collection<? extends GrantedAuthority> authorities, boolean isEnabled)
    {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.isEnabled = isEnabled;
    }

    public String getEmail() {
        return email;
    }

    public Integer id()
    {
        return id;
    }

    public String name()
    {
        return name;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true; // Assuming no account expiration logic
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true; // Assuming no account locking logic
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true; // Assuming no credential expiration logic
    }

    @Override
    public boolean isEnabled()
    {
        return isEnabled;
    }


}
