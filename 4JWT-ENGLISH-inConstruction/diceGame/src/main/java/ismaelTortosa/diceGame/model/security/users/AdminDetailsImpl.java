package ismaelTortosa.diceGame.model.security.users;

import ismaelTortosa.diceGame.model.domain.AdminEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
@AllArgsConstructor
@Service
public class AdminDetailsImpl implements UserDetails {
    private final AdminEntity adminEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return adminEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return adminEntity.getName();
    }

    public int getIdAdmin(){
        return adminEntity.getId_admin();
    }

    public String getRoleAdmin(){
        return adminEntity.getRoleAdmin();
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
