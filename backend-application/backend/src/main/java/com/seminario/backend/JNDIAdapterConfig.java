package com.seminario.backend;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableTransactionManagement
public class JNDIAdapterConfig {

    @Primary
    @Bean(name = "dsSeminario")
    public DataSource dataSource() throws NamingException {
        return getDataSourceByName(seminario().getJndiName());
    }
    @Bean(name = "walmartdb")
    public DataSource secondaryDataSource() {

        return getDataSourceByName(walmart().getJndiName());
    }
    @Bean(name = "waltmartem")
    public LocalContainerEntityManagerFactoryBean storingEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("walmartdb") DataSource ds) {
        return builder
                .dataSource(ds)
                .packages("com.seminario.backend.dto")
                .persistenceUnit("waltmartempu")
                .build();
    }



    @Bean@ConfigurationProperties(prefix = "seminario.datasource")
    public JndiPropertyHolder seminario() {
        return new JndiPropertyHolder();
    }
    @Bean@ConfigurationProperties(prefix = "secondarydatabase.datasource")
    public JndiPropertyHolder walmart() {
        return new JndiPropertyHolder();
    }

    private static class JndiPropertyHolder {
        private String jndiName;

        public String getJndiName() {
               return jndiName;
        }

        public void setJndiName(String jndiName) {
           this.jndiName = jndiName;
        }
   }
    @Bean
    public SpringLiquibase liquibase()throws NamingException {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:db/liquibase-changelog.xml");
        liquibase.setDataSource(dataSource());
        return liquibase;
    }

    public DataSource getDataSourceByName(String name){

        Context c;
        try
        {
            c = new InitialContext();
            return (DataSource) c.lookup("java:comp/env/" + name);
        }
        catch (NamingException e)
        {
            e.printStackTrace();
        }
        return null;}
}
