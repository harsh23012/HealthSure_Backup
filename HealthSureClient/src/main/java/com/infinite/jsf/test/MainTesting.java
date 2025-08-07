package com.infinite.jsf.test;

import com.infinite.jsf.util.EncryptPassword;

public class MainTesting {

    public static void main(String[] args) {
    	
       String code =  EncryptPassword.getCode("Harshkumar@23");
       System.out.println(code);
        
    }

  
}
