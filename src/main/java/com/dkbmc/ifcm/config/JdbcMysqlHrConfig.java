package com.dkbmc.ifcm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * fileName     : JdbcMysqlHrConfig
 * author       : inayoon
 * date         : 2023-03-29
 * description  : HR DB 설정
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-29       inayoon            최초생성
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.dkbmc.ifcm.domain.legacy",
                "com.dkbmc.ifcm.repository.legacy",
                "com.dkbmc.ifcm.controller.legacy",
                "com.dkbmc.ifcm.service.legacy"
        },
        entityManagerFactoryRef = "mysqlHrManagerFactory",
        transactionManagerRef = "mysqlHrTransactionManager"
)
public class JdbcMysqlHrConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mysql-hr")
    public DataSource mysqlHrDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mysqlHrManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(mysqlHrDataSource());

        localContainerEntityManagerFactoryBean.setPackagesToScan(
                new String[]{
                        "com.dkbmc.ifcm.domain.legacy",
                        "com.dkbmc.ifcm.repository.legacy",
                        "com.dkbmc.ifcm.controller.legacy",
                        "com.dkbmc.ifcm.service.legacy"
                }
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        // Hibernate 설정
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.ddl-auto","none");
        properties.put("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager mysqlHrTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mysqlHrManagerFactory().getObject());
        return transactionManager;
    }
}