package pl.sggw.support.webservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Kamil on 2017-10-20.
 */
@Configuration
@ComponentScan(
        basePackages = {
                "pl.sggw.support.webservice.service",
                "pl.sggw.support.webservice.dao",
                "pl.sggw.support.webservice.controller",
                "pl.sggw.support.webservice.populator",
                "pl.sggw.support.webservice.security"
        })
public class SpringConfiguration {


}
