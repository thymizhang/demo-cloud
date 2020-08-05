package com.demo.project.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 配置日志输出主机信息(主机名,主机地址)<br/>
 * 1.在日志配置文件logback-spring.xml里加入输出主机信息类
 * <pre>{@code  <conversionRule conversionWord="host" converterClass="com.xxxx.LogHostInfoConfig " /> }</pre>
 * 2.在需要用到主机信息时, 在pattern中加入%host<br/>
 * 例如: %d{yyyy-MM-dd HH:mm:ss:SSS} %host %green([%thread]) %highlight(%-5level) %boldMagenta(%logger{50}) - %cyan(%msg%n)
 *
 * @Author thymi
 * @Date 2020/5/9
 */
public class LogHostInfoConfig extends ClassicConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogHostInfoConfig.class);

    @Override
    public String convert(ILoggingEvent event) {
        try {
            // 主机名
            String hostname = InetAddress.getLocalHost().getHostName();
            // 主机ip
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            return hostname;
        } catch (UnknownHostException e) {
            LOGGER.error("获取日志Ip异常", e);
        }
        return null;
    }
}