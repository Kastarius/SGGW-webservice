package pl.sggw.support.webservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import pl.sggw.support.webservice.model.RoleModel;
import pl.sggw.support.webservice.model.UserModel;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Kamil on 2017-10-21.
 */
public class UserAuthentication implements Authentication {

    private final UserModel user;
    private boolean authenticated = true;

    public UserAuthentication(UserModel user) {
        this.user = user;
    }

    @Override
    public Set<RoleModel> getAuthorities() {
        return this.user.getPermissions();
    }

    @Override
    public String getCredentials() {
        return this.user.getPassword();
    }

    @Override
    public UserModel getDetails() {
        return this.user;
    }

    @Override
    public String getPrincipal() {
        return this.user.getLogin();
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return this.user.getLogin();
    }
}
