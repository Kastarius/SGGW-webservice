package pl.sggw.support.webservice.model;

import javax.persistence.*;

@Entity
@Table(name = "Status")
public class StatusModel extends ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "StatusId")
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
