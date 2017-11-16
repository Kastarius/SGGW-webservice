package pl.sggw.support.webservice.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Zgloszenie")
public class TaskModel extends ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TASK_SEQ")
    @Column(name = "ZgloszenieId")
    private long id;

    @Column(name = "DataZgloszenia")
    private Date date;

    @Column(name = "Tytul", unique = true, nullable = false)
    private String title;

    @Column(name = "Opis")
    private String description;

    @OneToOne
    @JoinColumn(name = "PriorytetId", nullable = false)
    private PriorityModel priorityModel;

    @OneToOne
    @JoinColumn(name = "UserId")
    private UserModel userModel;

    @OneToOne
    @JoinColumn(name = "KategoriaId")
    private CategoryModel categoryModel;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PriorityModel getPriorityModel() {
        return priorityModel;
    }

    public void setPriorityModel(PriorityModel priorityModel) {
        this.priorityModel = priorityModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }
}
