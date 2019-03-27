package com.seminario.services;

import com.seminario.backend.BackendApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;

/**
 * User: fcatania
 * Date: 27/3/2019
 * Time: 08:42
 */
@SpringBootApplication
@Import({BackendApplication.class})
public class WebserviceApplication  extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args){
        SpringApplication.run(WebserviceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebserviceApplication.class);
    }
}