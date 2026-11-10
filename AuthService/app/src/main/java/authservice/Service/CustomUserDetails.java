package authservice.Service;

import authservice.Entities.UserInfo;
import authservice.Entities.UserRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetails extends UserInfo implements UserDetails {
    private String username;

    private String password;

    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUserName) {
       this.username = byUserName.getUsername();
       this.password = byUserName.getPassword();
       List<GrantedAuthority> auths = new ArrayList<>();

       for (UserRoles userRoles : byUserName.getRoles()) {
            auths.add(new SimpleGrantedAuthority(userRoles.getName().toUpperCase()));
       }
       this.authorities = auths;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
