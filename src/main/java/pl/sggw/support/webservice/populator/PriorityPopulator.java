package pl.sggw.support.webservice.populator;

import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dto.Priority;
import pl.sggw.support.webservice.model.PriorityModel;

@Component
public class PriorityPopulator extends AbstractPopulator<PriorityModel, Priority> {

    @Override
    public void populate(PriorityModel source, Priority target) {
        target.setId(source.getId());
        target.setName(source.getName());
    }

    @Override
    public void reversePopulate(Priority source, PriorityModel target) {
        target.setId(source.getId());
        target.setName(source.getName());
    }
}
