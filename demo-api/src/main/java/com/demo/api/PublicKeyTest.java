package com.demo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author thymi
 * @Date 2020/7/23
 */
public class PublicKeyTest {
    public static void main(String[] args) {
        String uri = "http://localhost:8080/auth/pubkey";
        String key = new RestTemplate().getForObject(uri, String.class);
        System.out.println(key);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map map = objectMapper.readValue(key, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
