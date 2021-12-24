package com.linbo.boot.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername("lin");
        userDetails.setPassword("$2a$10$01DTAXx0wcTMBAWFp24cWuv.oSfVbPrJKO/g8XJLiMJSl08h.TKiu");
        return userDetails;
    }

    @Data
    class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
        private String username;
        private String password;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
