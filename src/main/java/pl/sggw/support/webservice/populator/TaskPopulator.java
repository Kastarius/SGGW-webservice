package pl.sggw.support.webservice.populator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dto.*;
import pl.sggw.support.webservice.model.*;

import java.util.List;

@Component
public class TaskPopulator extends AbstractPopulator<TaskModel, Task> {

    @Autowired
    PriorityPopulator priorityPopulator;

    @Autowired
    CategoryPopulator categoryPopulator;

    @Autowired
    CommentPopulator commentPopulator;
  
    @Autowired
    StatusPopulator statusPopulator;

    @Override
    public void populate(TaskModel source, Task target) {
        target.setId(source.getId());
        target.setDate(source.getDate());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());

        Priority priority = new Priority();
        PriorityModel priorityModel = source.getPriorityModel();
        priority.setId(priorityModel.getId());
        priority.setName(priorityModel.getName());
        target.setPriority(priority);

        Category category = new Category();
        CategoryModel categoryModel = source.getCategoryModel();
        category.setId(categoryModel.getId());
        category.setName(categoryModel.getName());
        target.setCategory(category);

        BasicUserData userData = new BasicUserData();
        UserModel userModel = source.getUserModel();
        userData.setId(userModel.getId());
        userData.setFirstName(userModel.getFirstName());
        userData.setLastName(userModel.getLastName());
        target.setUserData(userData);

        List<CommentModel> commentModels = source.getComments();
        List<Comment> comments = target.getComments();
        commentModels.stream().forEach(commentModel -> {
            Comment c = new Comment();
            commentPopulator.populate(commentModel, c);
            comments.add(c);
        });
        target.setComments(comments);
      
        Status status = new Status();
        StatusModel statusModel = source.getStatusModel();
        status.setId(statusModel.getId());
        status.setName(statusModel.getName());
        target.setStatus(status);
    }

    @Override
    public void reversePopulate(Task source, TaskModel target) {
        target.setId(source.getId());
        target.setDate(source.getDate());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setPriorityModel(convertToPriorityModel(source.getPriority()));
        target.setCategoryModel(convertToCategoryModel(source.getCategory()));
        target.setStatusModel(convertToStatusModel(source.getStatus()));

        BasicUserData userData = source.getUserData();
        UserModel userModel = new UserModel();
        userModel.setId(userData.getId());
        userModel.setFirstName(userData.getFirstName());
        userModel.setLastName(userData.getLastName());
        target.setUserModel(userModel);

        List<Comment> comments = source.getComments();
        List<CommentModel> commentModels = target.getComments();
        comments.stream().forEach(comment -> {
            CommentModel cm = new CommentModel();
            BasicUserData userData1 = comment.getUserData();
            UserModel userModel1 = new UserModel();
            userModel1.setId(userData1.getId());
            commentPopulator.reversePopulate(comment, cm, userModel, target);
            commentModels.add(cm);
        });
        target.setComments(commentModels);
    }

    private PriorityModel convertToPriorityModel(Priority priority) {
        PriorityModel priorityModel = new PriorityModel();
        priorityPopulator.reversePopulate(priority, priorityModel);
        return priorityModel;
    }

    private CategoryModel convertToCategoryModel(Category category) {
        CategoryModel categoryModel = new CategoryModel();
        categoryPopulator.reversePopulate(category, categoryModel);
        return categoryModel;
    }

    private StatusModel convertToStatusModel(Status status) {
        StatusModel statusModel = new StatusModel();
        statusPopulator.reversePopulate(status, statusModel);
        return statusModel;
    }
}
