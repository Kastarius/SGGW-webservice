package pl.sggw.support.webservice.populator;

import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dto.BasicUserData;
import pl.sggw.support.webservice.dto.Comment;
import pl.sggw.support.webservice.model.CommentModel;
import pl.sggw.support.webservice.model.TaskModel;
import pl.sggw.support.webservice.model.UserModel;

@Component
public class CommentPopulator {

    public void populate(CommentModel source, Comment target) {
        target.setId(source.getId());
        target.setContent(source.getContent());
        target.setDate(source.getDate());

        BasicUserData userData = new BasicUserData();
        UserModel userModel = source.getUser();
        userData.setId(userModel.getId());
        userData.setFirstName(userModel.getFirstName());
        userData.setLastName(userModel.getLastName());

        target.setUserData(userData);
    }

    public void reversePopulate(Comment source, CommentModel target, UserModel userModel, TaskModel taskModel) {
        target.setId(source.getId());
        target.setContent(source.getContent());
        target.setDate(source.getDate());
        target.setUser(userModel);
        target.setTask(taskModel);
    }

}
