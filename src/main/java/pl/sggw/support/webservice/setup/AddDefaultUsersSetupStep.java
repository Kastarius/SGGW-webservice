package pl.sggw.support.webservice.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dao.UserDAO;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.setup.exception.SetupException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil on 2017-11-02.
 */
@Component
@Order(value=2) // Order in setup procedure
public class AddDefaultUsersSetupStep extends AbstractApplicationSetupStep {

    private static final Logger LOG = LoggerFactory.getLogger(AddDefaultUsersSetupStep.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    public void perform() throws SetupException {
        LOG.info("Started executing "+AddDefaultUsersSetupStep.class.getSimpleName());
        try {
            List<UserModel> models = Optional.ofNullable(importObjects(UserModel.class, "import\\user.csv")).orElse(new ArrayList<>());
            models.forEach(user -> LOG.debug(String.format("User:[id:%s;login:%s;password:%s;firstName:%s;lastName:%s;email:%s;phone:%s]", user.getId(), user.getLogin(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone())));

            models.forEach(userDAO::save);
        } catch (Exception e){
            throw new SetupException(e);
        }
    }
}
