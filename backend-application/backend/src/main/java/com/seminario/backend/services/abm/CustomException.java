package com.seminario.backend.services.abm;

public class CustomException extends Exception {

    public CustomException(String str){
        super(str);
    }

    public CustomException(String s, Throwable t){ super(s,t);}

}

