package com.dkbmc.ifcm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * fileName     : JdbcMariaMapConfig
 * author       : inayoon
 * date         : 2023-03-29
 * description  : Mapping DB 설정
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-29       inayoon            최초생성
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.dkbmc.ifcm.domain.dbconn",
                "com.dkbmc.ifcm.repository.dbconn",
                "com.dkbmc.ifcm.controller.dbconn",
                "com.dkbmc.ifcm.service.dbconn"
        },
        entityManagerFactoryRef = "mariaMapManagerFactory",
        transactionManagerRef = "mariaMapTransactionManager"
)
public class JdbcMariaMapConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.maria-map")
    public DataSource mariaMapDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mariaMapManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(mariaMapDataSource());

        localContainerEntityManagerFactoryBean.setPackagesToScan(
                new String[]{
                        "com.dkbmc.ifcm.domain.dbconn",
                        "com.dkbmc.ifcm.repository.dbconn",
                        "com.dkbmc.ifcm.controller.dbconn",
                        "com.dkbmc.ifcm.service.dbconn"
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
    @Primary
    public PlatformTransactionManager mariaMapTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mariaMapManagerFactory().getObject());
        return transactionManager;
    }
}