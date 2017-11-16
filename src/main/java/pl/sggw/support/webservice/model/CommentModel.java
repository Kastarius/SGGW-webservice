package pl.sggw.support.webservice.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Komentarz")
public class CommentModel extends ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "KomentarzId")
    private long id;
    @Column(name = "Komentarz")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UzytkownikId")
    private UserModel user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ZgloszenieId")
    private TaskModel task;
    @Column(name = "DataKomentarza")
    private Date date;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TaskModel getTask() {
        return task;
    }

    public void setTask(TaskModel task) {
        this.task = task;
    }
}
