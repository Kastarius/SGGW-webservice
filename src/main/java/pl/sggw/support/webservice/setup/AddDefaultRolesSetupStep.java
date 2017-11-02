package pl.sggw.support.webservice.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dao.RoleDAO;
import pl.sggw.support.webservice.model.RoleModel;
import pl.sggw.support.webservice.setup.exception.SetupException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kamil on 2017-11-02.
 */
@Component
@Order(value=1) // Order in setup procedure
public class AddDefaultRolesSetupStep extends AbstractApplicationSetupStep {

    private static final Logger LOG = LoggerFactory.getLogger(AddDefaultRolesSetupStep.class);

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public void perform() throws SetupException {
        LOG.info("Started executing "+AddDefaultRolesSetupStep.class.getSimpleName());
        try {
            List<RoleModel> models = Optional.ofNullable(importObjects(RoleModel.class, "import\\role.csv")).orElse(new ArrayList<>());
            models.forEach(role -> LOG.debug(String.format("Role:[id:%s;name:%s;code:%s]", role.getId(), role.getName(), role.getCode())));

            models.forEach(roleDAO::save);
        } catch (Exception e){
            throw new SetupException(e);
        }
    }
}
