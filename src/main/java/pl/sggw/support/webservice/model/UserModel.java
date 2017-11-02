package pl.sggw.support.webservice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kamil on 2017-10-21.
 */
@Entity
@Table(name = "Uzytkownik")
public class UserModel extends ItemModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UzytkownikID")
    private long id;
    @Column(name = "Login", unique = true, nullable = false)
    private String login;
    @Column(name = "Haslo", nullable = false)
    private String password;
    @Column(name = "Imie")
    private String firstName;
    @Column(name = "Nazwisko")
    private String lastName;
    @Column(name = "Email")
    private String email;
    @Column(name = "Telefon")
    private String phone;
    @ManyToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="RolaUzytkownika", joinColumns=@JoinColumn(name="UzytkownikID"), inverseJoinColumns=@JoinColumn(name="RolaID"))
    private Set<RoleModel> permissions;

    public UserModel() {
    }

    @Override
    public Long getId() {
        return this.id ;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Set<RoleModel> getAuthorities() {
        return permissions;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.getLogin();
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<RoleModel> getPermissions() {
        if(this.permissions == null){
            this.permissions = new HashSet<>();
        }
        return permissions;
    }

    public void setPermissions(Set<RoleModel> permissions) {
        this.permissions = permissions;
    }
}
