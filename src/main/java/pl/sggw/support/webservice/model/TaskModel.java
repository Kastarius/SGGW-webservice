package pl.sggw.support.webservice.model;

import javax.persistence.*;

@Entity
@Table(name = "Zgloszenie")
public class TaskModel extends ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Override
    public Long getId() {
        return id;
    }
}
