package pl.sggw.support.webservice.populator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dto.Role;
import pl.sggw.support.webservice.dto.User;
import pl.sggw.support.webservice.model.RoleModel;
import pl.sggw.support.webservice.model.UserModel;

import java.util.Objects;

import static java.util.stream.Collectors.*;

/**
 * Created by Kamil on 2017-10-22.
 */
@Component
public class UserPopulator extends AbstractPopulator<UserModel,User> {

    @Autowired
    private RolePopulator rolePopulator;

    @Override
    public void populate(UserModel source, User target) {
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        if(Objects.nonNull(source.getPermissions()))target.setPermissions(source.getPermissions()
                .stream()
                .map(this::convertToRoleDTO)
                .collect(toList()));
    }

    @Override
    public void reversePopulate(User source, UserModel target) {
        target.setLogin(source.getLogin());
        target.setPassword(source.getPassword());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setPhone(source.getPhone());
        if(Objects.nonNull(source.getPermissions()))target.setPermissions(source.getPermissions()
                .stream()
                .map(this::convertToRoleModel)
                .collect(toSet()));
    }

    private Role convertToRoleDTO(RoleModel model){
        Role role = new Role();
        rolePopulator.populate(model,role);
        return role;
    }

    private RoleModel convertToRoleModel(Role role){
        RoleModel model = new RoleModel();
        rolePopulator.reversePopulate(role,model);
        return model;
    }
}
