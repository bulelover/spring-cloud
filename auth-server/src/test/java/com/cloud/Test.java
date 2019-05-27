package com.cloud;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class Test {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder("spring-cloud-demo");

        System.out.println(passwordEncoder.matches("guest","f9d8d852bd85e3321ecefb241571084d60a70785c57bc57a8c10e2525c0274d980eecc5048123587"));
        System.out.println(passwordEncoder.matches("admin","1236d137fd172c1dca9c0bbb2d3a754b3c6e211f3d49bd155e9e8dac1be174a53998705667db90d7"));
        System.out.println("====================guest : "+passwordEncoder.encode("guest"));
        System.out.println("====================admin : "+passwordEncoder.encode("admin"));
    }
}
