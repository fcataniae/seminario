package com.seminario.services.rest;



import org.springframework.web.bind.annotation.*;
/**
 * User: fcatania
 * Date: 27/3/2019
 * Time: 08:35
 */

@RestController
@RequestMapping("/service")
public class ApiRestController {

    private ApiRestController(){};

    @GetMapping("/getprueba")
    public String getPrueba(){
        return "Prueba";
    }

}