package com.blbz.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@ComponentScan("com.blbz")
@EnableJpaRepositories(basePackages = "com.blbz.repository")
public class JPAConfiguration {

     @Bean
     public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
         final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
         entityManagerFactoryBean.setDataSource(dataSource());
         entityManagerFactoryBean.setPackagesToScan("com.blbz.entity");
         final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
         entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
         entityManagerFactoryBean.setJpaProperties(additionalProperties());

         return entityManagerFactoryBean;
     }
     final Properties additionalProperties() {
         final Properties hibernateProperties = new Properties();
         hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
         hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
         hibernateProperties.setProperty("hibernate.show_sql", "true");
         hibernateProperties.setProperty("hibernate.format_sq", "true");
 /*        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");*/
        return hibernateProperties;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/loginDB?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }


        @Bean
        public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
            final JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(emf);
            return transactionManager;
        }



    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}