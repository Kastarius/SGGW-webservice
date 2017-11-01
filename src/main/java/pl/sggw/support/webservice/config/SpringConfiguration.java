package pl.sggw.support.webservice.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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


        @Bean
        public static PropertyPlaceholderConfigurer properties() {
                PropertyPlaceholderConfigurer ppc  = new PropertyPlaceholderConfigurer();
                Resource[] resources = new ClassPathResource[]{new ClassPathResource("application.properties")
                        ,new ClassPathResource("local.properties")};
                ppc.setLocations(resources);
                ppc.setIgnoreUnresolvablePlaceholders(true);
                ppc.setIgnoreResourceNotFound(true);
                return ppc;
        }


}
