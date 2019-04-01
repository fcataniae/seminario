package com.seminario.services.rest;



import com.seminario.backend.dto.DTOUser;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
/**
 * User: fcatania
 * Date: 27/3/2019
 * Time: 08:35
 */

@RestController
@RequestMapping("/service")
@CrossOrigin
@Scope("session")
public class ApiRestController {

    private ApiRestController(){};

    @PostMapping("/login")
    public DTOUser login(@RequestHeader("Authorization") String auth){

        System.out.println(auth);
        DTOUser user = new DTOUser("Franco","Catania","cataniafrane@gmail.com","137854");

        return user;
    }
    @GetMapping("/prueba")
    public String prueba(){
        return "la prueba funciona!";
    }

}