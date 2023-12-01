package com.example.doan;

import com.example.doan.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DoAnApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoAnApplication.class, args);
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringApplicationContext stringApplicationContext(){
        return  new SpringApplicationContext();
    }

    @Bean(name="AppProperties")
    public AppProperties getAppProperties(){
        return  new AppProperties();
    }

}
