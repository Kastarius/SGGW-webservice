package pl.sggw.support.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.dao.PriorityDAO;
import pl.sggw.support.webservice.dto.Priority;
import pl.sggw.support.webservice.model.PriorityModel;
import pl.sggw.support.webservice.populator.PriorityPopulator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PriorityService {

    @Autowired
    private PriorityPopulator populator;

    @Autowired
    private PriorityDAO dao;

    public List<Priority> getAll() {
        return dao.getAllPriorities().stream().map(this::convert).collect(Collectors.toList());
    }

    public Priority save(Priority priority) {
        PriorityModel model = new PriorityModel();
        populator.reversePopulate(priority, model);
        dao.save(model);
        return priority;
    }

    public Priority saveOrUpdate(Priority priority) {
        PriorityModel priorityModel = new PriorityModel();
        populator.reversePopulate(priority, priorityModel);
        save(priority);
        populator.populate(priorityModel, priority);
        return priority;
    }

    public void remove(long id) {
        dao.remove(dao.getPriorityByID(id));
    }

    private Priority convert(PriorityModel model){
        Priority priority = new Priority();
        if(Objects.nonNull(model))populator.populate(model, priority);
        return priority;
    }
}
