package com.example.doan.security;


import com.example.doan.SpringApplicationContext;

public class SecurityContants {
    public static final long EXPIRATION_TIME=864000000;
    public static final String TOKEN_PREFIX="Bearer ";
    public static final String HEADER_STRING="Authorization";
    public static final String SIGN_UP_URL="/auth";

    public static String getTokenSecret(){
        AppProperties appProperties=(AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
