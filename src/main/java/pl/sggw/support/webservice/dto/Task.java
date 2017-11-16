package pl.sggw.support.webservice.dto;

import java.util.Date;

public class Task {

    private long id;
    private Date date;
    private String title;
    private String description;
    private Category category;
    private BasicUserData userData;
    private Priority priority;

    public long getId() {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BasicUserData getUserData() {
        return userData;
    }

    public void setUserData(BasicUserData userData) {
        this.userData = userData;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
