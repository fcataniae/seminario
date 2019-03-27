package com.seminario.backend;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableTransactionManagement
@Profile("produccion")
public class JNDIAdapterConfig {

    @Primary
    @Bean(name = "dsSeminario",destroyMethod = "")
    public DataSource dataSource() throws NamingException {
        InitialContext context = new InitialContext();
        return (DataSource) context.lookup(precalificador().getJndiName());
    }

    @Bean@ConfigurationProperties(prefix = "seminario.datasource")
    public JndiPropertyHolder precalificador() {
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

}
