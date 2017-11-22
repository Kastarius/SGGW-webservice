package pl.sggw.support.webservice.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.sggw.support.webservice.dao.StatusDAO;
import pl.sggw.support.webservice.model.StatusModel;
import pl.sggw.support.webservice.setup.exception.SetupException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddDefaultStatusesSetupStep extends AbstractApplicationSetupStep {

    private static final Logger LOG = LoggerFactory.getLogger(AddDefaultStatusesSetupStep.class);

    @Autowired
    StatusDAO statusDAO;

    @Override
    public void perform() throws SetupException {
        LOG.info("Started executing " + AddDefaultStatusesSetupStep.class.getSimpleName());
        try {
            List<StatusModel> models = Optional.ofNullable(importObjects(StatusModel.class, "import\\status.csv")).orElse(new ArrayList<>());
            models.forEach(statusDAO::save);
        } catch (Exception e) {
            throw new SetupException(e);
        }

    }
}
