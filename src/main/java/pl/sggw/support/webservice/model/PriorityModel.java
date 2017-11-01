package pl.sggw.support.webservice.model;

import javax.persistence.*;

@Entity
@Table(name = "Priorytet")
public class PriorityModel extends ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PriorytetId")
    private long id;
    @Column(name = "Nazwa")
    private String name;

    @Override
    public Long getId() {
        return id;
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
}
