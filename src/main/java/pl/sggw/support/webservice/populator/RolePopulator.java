package pl.sggw.support.webservice.populator;

import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dto.Role;
import pl.sggw.support.webservice.model.RoleModel;

/**
 * Created by Kamil on 2017-10-22.
 */
@Component
public class RolePopulator extends AbstractPopulator<RoleModel,Role> {

    @Override
    public void populate(RoleModel source, Role target) {
        target.setId(source.getId());
        target.setName(source.getName());
        target.setCode(source.getCode());
    }

    @Override
    public void reversePopulate(Role source, RoleModel target) {
        target.setId(source.getId());
        target.setName(source.getName());
        target.setCode(source.getCode());
    }
}
