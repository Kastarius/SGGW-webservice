package pl.sggw.support.webservice.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Created by Kamil on 2017-10-21.
 */
@Entity
@Table(name = "Rola")
public class RoleModel extends ItemModel implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RolaID")
    private long id;
    @Column(name = "Nazwa")
    private String name;
    @Column(name = "Kod", unique = true, nullable = false)
    private String code;

    @Override
    public Long getId() {
        return this.id ;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getAuthority() {
        return "ROLE_"+this.code;
    }
}
