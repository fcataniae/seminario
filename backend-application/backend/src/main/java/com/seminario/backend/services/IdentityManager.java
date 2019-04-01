package com.seminario.backend.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * User: fcatania
 * Date: 28/3/2019
 * Time: 13:04
 */
@Service
public class IdentityManager {

    Logger log = Logger.getLogger("wsSeminario");

    public boolean authenticateUser(String name, String password) {
        System.out.println("name "+name + " pass " + password);
        return name.equals("fcatania") && password.equals("pass");
    }
}