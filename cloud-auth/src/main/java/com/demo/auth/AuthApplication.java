package com.demo.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密方法测试
 *
 * @Author thymi
 * @Date 2020/5/28
 */
public class AuthApplication {
    public static void main(String[] args) {
        String pass = "111111";
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(pass);
        System.out.println(hashPass);

        boolean f = bcryptPasswordEncoder.matches("secret","$2a$10$QlVCrfYGe5E.2ToP3t/LGOZPyX1WCcN6IMSg3vekQTGyqx9Zse7J.");
        System.out.println(f);
    }
}
