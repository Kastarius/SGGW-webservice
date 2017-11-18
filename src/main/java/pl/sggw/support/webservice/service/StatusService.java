package pl.sggw.support.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.dao.StatusDAO;
import pl.sggw.support.webservice.dto.Status;
import pl.sggw.support.webservice.model.StatusModel;
import pl.sggw.support.webservice.populator.StatusPopulator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("statusService")
public class StatusService {

    @Autowired
    StatusDAO statusDAO;

    @Autowired
    StatusPopulator statusPopulator;

    public List<Status> getAll() {
        return statusDAO.getAllStatuses().stream().map(this::convert).collect(Collectors.toList());
    }

    private Status convert(StatusModel statusModel) {
        Status status = new Status();
        if(Objects.nonNull(statusModel))statusPopulator.populate(statusModel, status);
        return status;
    }

    public void remove(long id) {
        statusDAO.remove(statusDAO.getStatusById(id));
    }

    public Status save(Status status) {
        StatusModel model = new StatusModel();
        statusPopulator.reversePopulate(status, model);
        statusDAO.save(model);
        return status;
    }

    public Status saveOrUpdate(Status status) {
        StatusModel statusModel = new StatusModel();
        statusPopulator.reversePopulate(status, statusModel);
        save(status);
        statusPopulator.populate(statusModel, status);
        return status;
    }

    public Status getStatus(String id) {
        return convert(statusDAO.getStatusById(Long.valueOf(id)));
    }
}
