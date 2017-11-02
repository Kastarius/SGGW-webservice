package pl.sggw.support.webservice.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dao.RoleDAO;
import pl.sggw.support.webservice.dao.UserDAO;
import pl.sggw.support.webservice.model.RoleModel;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.setup.exception.SetupException;

import java.util.*;

/**
 * Created by Kamil on 2017-11-02.
 */
@Component
@Order(value=3) // Order in setup procedure
public class AddRoleToUserSetupStep extends AbstractApplicationSetupStep {

    private static final Logger LOG = LoggerFactory.getLogger(AddRoleToUserSetupStep.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public void perform() throws SetupException {
        LOG.info("Started executing "+AddRoleToUserSetupStep.class.getSimpleName());
        try {
            List<String[]> data = Optional.ofNullable(importData("import\\user_role.csv")).orElse(new ArrayList<>());
            data.forEach(item -> LOG.debug(Arrays.toString(item)));

            data.forEach(item -> {
                UserModel user = userDAO.getUserByLogin(item[0]);
                RoleModel role = roleDAO.getRoleByCode(item[1]);
                if (Objects.nonNull(user) && Objects.nonNull(role)) {
                    user.getPermissions().add(role);
                    userDAO.save(user);
                }
            });
        } catch (Exception e){
            throw new SetupException(e);
        }
    }
}
