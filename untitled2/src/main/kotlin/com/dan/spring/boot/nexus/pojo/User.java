package com.dan.spring.boot.nexus.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shop")
//@PropertySource("classpath:application-dev.properties")//这是属性文件路径
public class User {
    private String number;
    private String address;

    public User() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void talk(String word) {
        System.out.println(word);
    }

    public void walk() {
        System.out.println("walk");
    }

    @Override
    public String toString() {
        return "User{" +
                "number='" + number + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
