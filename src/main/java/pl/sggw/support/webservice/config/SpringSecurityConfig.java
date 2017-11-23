package pl.sggw.support.webservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.sggw.support.webservice.security.util.AnonymousUserHandler;
import pl.sggw.support.webservice.security.TokenAuthenticationFilter;
import pl.sggw.support.webservice.security.TokenAuthenticationService;

/**
 * Created by Kamil on 2017-10-21.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private  TokenAuthenticationService tokenAuthenticationService;

    public SpringSecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl().and()
                .authorizeRequests()
                .antMatchers("/auth/**","/swagger-ui.html","/swagger-ui/**","/webjars/**","/swagger-resources/**","/v2/**").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(new TokenAuthenticationFilter(tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new AnonymousUserHandler());
    }

    @Bean
    public static ShaPasswordEncoder passwordEncoder(){
        return new ShaPasswordEncoder(256);
    }
}
