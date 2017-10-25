package pl.sggw.support.webservice.dto;

import java.util.List;

/**
 * Created by Kamil on 2017-10-22.
 */
public class User {

    private long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Role> permissions;

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Role> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Role> permissions) {
        this.permissions = permissions;
    }
}
