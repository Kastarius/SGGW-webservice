package pl.sggw.support.webservice.setup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.setup.AbstractApplicationSetupStep;
import pl.sggw.support.webservice.setup.exception.SetupException;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Kamil on 2017-11-02.
 */
@Service
public class ApplicationSetupService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationSetupService.class);

    @Autowired
    private List<AbstractApplicationSetupStep> setupSteps;


    public void performSetup(){
        final boolean[] isSuccess = {true};
        LOG.info("***************************************************");
        LOG.info("           Application init setup started          ");
        LOG.info("***************************************************");
        setupSteps.forEach(setupSteps -> {
            try {
                setupSteps.perform();
            } catch (SetupException e) {
                isSuccess[0] = false;
                LOG.error(e.getMessage(),e);
            }
        });
        LOG.info("***************************************************");
        LOG.info("      Application init setup ended with "+(isSuccess[0]?"success":"error")+"           ");
        LOG.info("***************************************************");
    }
}
