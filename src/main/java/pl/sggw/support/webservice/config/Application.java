package pl.sggw.support.webservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.sggw.support.webservice.setup.service.ApplicationSetupService;


/**
 * Created by Kamil on 2017-10-20.
 */
@SpringBootApplication
@EnableScheduling
public class Application implements ApplicationRunner{

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ApplicationSetupService setupService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        resolveNonOptionArgsActivities(applicationArguments);

    }

    private void resolveNonOptionArgsActivities(ApplicationArguments applicationArguments) {
        for (String name : applicationArguments.getNonOptionArgs()){
            if("init".equals(name))setupService.performSetup();
        }
    }
}
