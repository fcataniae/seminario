package com.seminario.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * User: fcatania
 * Date: 27/3/2019
 * Time: 08:42
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.seminario.backend"})
@EnableJpaRepositories(basePackages = {"com.seminario.backend"})
@EntityScan(basePackages = {"com.seminario.backend"})
@Import(EclipseLinkConfiguration.class)
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}