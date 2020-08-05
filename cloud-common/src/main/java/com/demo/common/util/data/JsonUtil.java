package com.demo.common.util.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * json工具类, json与对象互转
 *
 * @Author thymi
 * @Date 2020/7/21
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // 对于空的对象转json的时候不抛出错误
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 对LocalDateTime对象序列化
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        // 允许属性名称没有引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置输出时包含属性的风格
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    /**
     * 序列化，将对象转化为json字符串
     *
     * @param data
     * @return
     */
    public static String toJsonString(Object data) {
        if (data == null) {
            return null;
        }

        String json = null;
        try {
            json = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("[{}] toJsonString error：{{}}", data.getClass().getSimpleName(), e);
        }
        return json;
    }

    /**
     * 反序列化，将json字符串转化为对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse(@NonNull String json, Class<T> clazz) {
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error(" parse json [{}] to class [{}] error：{{}}", json, clazz.getSimpleName(), e);
        }
        return t;
    }

    public static void main(String[] args) {
//        User user = new User();
//        user.setId("12345");
//        user.setUsername("thymi");
//        user.setCreated(LocalDateTime.now());
//
//        String json = JsonUtil.toJsonString(user);
//        System.out.println(json);
//        Map map = JsonUtil.parse(json, Map.class);
//        User parse = JsonUtil.parse(json, User.class);
//        System.out.println(parse.getCreated().format(DateUtil.FORMATTER_DATETIME_DASH));
    }
}
