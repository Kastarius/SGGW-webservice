package pl.sggw.support.webservice.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Zgloszenie")
public class TaskModel extends ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToMany(targetEntity = CommentModel.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> comments;
  
    @OneToOne
    @JoinColumn(name = "StatusId", nullable = false)
    private StatusModel statusModel;

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

    public List<CommentModel> getComments() {
        if (this.comments == null) {
            this.comments =  new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public void removeComment(CommentModel model) {
        comments.remove(model);
        if (model != null) {
            model.setTask(null);
        }
    }
      
    public StatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(StatusModel statusModel) {
        this.statusModel = statusModel;
    }
}
