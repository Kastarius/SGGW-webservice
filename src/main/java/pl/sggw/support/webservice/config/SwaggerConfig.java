package pl.sggw.support.webservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Kamil on 2017-10-25.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pl.sggw.support.webservice.controller"))
                .build()
                .apiInfo(metaData())
                .globalOperationParameters(Collections.singletonList(new ParameterBuilder()
                        .name("X-AUTH-TOKEN")
                        .description("Authentication token")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build()));
    }
    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "SGGW Support Webservice",
                "REST API for SGGW Support apps",
                "0.1",
                "Terms of service",
                new Contact("SGGW Support Webservice Teem", null, null),
                null,
                null);
        return apiInfo;
    }
}
