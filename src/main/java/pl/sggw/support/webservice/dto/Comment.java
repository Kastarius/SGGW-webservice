package pl.sggw.support.webservice.dto;

import java.util.Date;

public class Comment {

    private long id;
    private String content;
    private Date date;
    private BasicUserData userData;

    public long getId() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BasicUserData getUserData() {
        return userData;
    }

    public void setUserData(BasicUserData userData) {
        this.userData = userData;
    }

}
