package com.demo.api;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


/**
 * 测试jwt令牌解析
 *
 * @Author thymi
 * @Date 2020/7/22
 */
public class JwtTest {
    public static void main(String[] args) {
        // 生成jwt
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String jws = Jwts.builder().setSubject("gceasy").signWith(key).compact();
        System.out.println(jws);

        try {
            // 读取jwt中自定义的信息
            Object body = Jwts.parserBuilder().setSigningKey(key).build().parse(jws).getBody();
            Claims body1 = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody();
            System.out.println(body1.getSubject());
        } catch (JwtException e) {
            System.out.println(e.getMessage());
        }

    }
}
