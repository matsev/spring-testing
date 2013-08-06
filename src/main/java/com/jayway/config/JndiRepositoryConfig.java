package com.jayway.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Profile("prod")
@Configuration
@EnableJpaRepositories("com.jayway.repository")
public class JndiRepositoryConfig implements RepositoryConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    @Override
    public DataSource dataSource() {
        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();
        factoryBean.setJndiName("java:comp/env/jdbc/myds");
        factoryBean.setExpectedType(DataSource.class);
        return (DataSource) factoryBean.getObject();
    }

    @Bean
    @Override
    public FactoryBean entityManagerFactory() {
        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();
        factoryBean.setJndiName("persistence/bankDemo");
        factoryBean.setExpectedType(EntityManagerFactory.class);
        return factoryBean;
    }

    @Bean
    @Override
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
