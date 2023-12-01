package com.example.doan;

import com.example.doan.security.AppProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SpringApplicationContext implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT=applicationContext;
    }

    public static Object getBean(String beanName){
        return CONTEXT.getBean(beanName);
    }

}
