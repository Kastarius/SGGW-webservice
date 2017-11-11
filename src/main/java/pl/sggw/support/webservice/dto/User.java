package pl.sggw.support.webservice.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by Kamil on 2017-10-22.
 */
public class User {

    private long id;
    @Size(max = 255,message = "Login can be up to 255 characters")
    @NotEmpty(message = "Login cannot be empty")
    private String login;
    @Size(max = 255,message = "Password can be up to 255 characters")
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @Size(max = 255,message = "First name can be up to 255 characters")
    private String firstName;
    @Size(max = 255,message = "Last name can be up to 255 characters")
    private String lastName;
    @Size(max = 255,message = "Email address can be up to 255 characters")
    @Email(message = "Please provide a valid e-mail")
    private String email;
    @Size(max = 9,message = "Phone number can be up to 9 characters")
    @Digits(integer = 9,fraction = 0,message = "Phone number can contains only numbers")
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
