package pl.sggw.support.webservice.populator;

import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dto.Status;
import pl.sggw.support.webservice.model.StatusModel;

@Component
public class StatusPopulator extends AbstractPopulator<StatusModel, Status> {

    @Override
    public void populate(StatusModel source, Status target) {
        target.setId(source.getId());
        target.setName(source.getName());
    }

    @Override
    public void reversePopulate(Status source, StatusModel target) {
        target.setId(source.getId());
        target.setName(source.getName());
    }
}
