package pl.sggw.support.webservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * Created by Kamil on 2017-10-20.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {


    /**
     * Driver class
     */
    @Value("${db.driver}")
    private String dbDriverClassName;
    /**
     * Url to database
     */
    @Value("${db.url}")
    private String dbUrl;
    /**
     * Database user name
     */
    @Value("${db.username}")
    private String dbUsername;
    /**
     * Database user password
     */
    @Value("${db.password}")
    private String dbPassword;
    /**
     * Database dialect
     */
    @Value("${hibernate.dialect}")
    private String hibernateDialect;
    /**
     * logging of all the generated SQL statements
     */
    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;
    /**
     * Format the generated SQL statement to make it more readable
     */
    @Value("${hibernate.format_sql}")
    private String hibernateFormatSql;
    /**
     * Automatically validates or exports schema DDL to the database when the App starting
     */
    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource());

        entityManagerFactory.setPackagesToScan("pl.sggw.support.webservice.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        Properties additionalProperties = new Properties();
        additionalProperties.put("hibernate.dialect",  hibernateDialect);
        additionalProperties.put("hibernate.show_sql", hibernateShowSql);
        additionalProperties.put("hibernate.format_sql", hibernateFormatSql);
        additionalProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        entityManagerFactory.setJpaProperties(additionalProperties);

        return entityManagerFactory;
    }


    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }


    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
