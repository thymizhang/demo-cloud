package com.demo.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

/**
 * 虚拟机工具类, 获取虚拟机运行相关信息
 *
 * @Author thymi
 * @Date 2020/8/26
 */
@Slf4j
public class JvmUtil {

    /**
     * 获取JVM内存使用情况
     */
    public static void getMemoryStatus() {
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long mb = 1024 * 1024;
        String mega = " MB";

        long physicalMemory;
        try {
            physicalMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                    .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        } catch (Exception e) {
            physicalMemory = -1L;
        }

        int availableCores = Runtime.getRuntime().availableProcessors();

        log.info("========================== System Info ==========================");
        log.info("Java version: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        log.info("Operating system: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        log.info("CPU Cores: " + availableCores);
        if (physicalMemory != -1L) {
            log.info("Physical Memory: " + format.format(physicalMemory / mb) + mega);
        }
        log.info("========================== JVM Memory Info ==========================");
        log.info("Max allowed memory: " + format.format(maxMemory / mb) + mega);
        log.info("Allocated memory:" + format.format(allocatedMemory / mb) + mega);
        log.info("Used memory in allocated: " + format.format((allocatedMemory - freeMemory) / mb) + mega);
        log.info("Free memory in allocated: " + format.format(freeMemory / mb) + mega);
        log.info("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
        log.info("Heap Memory Usage: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage());
        log.info("Non-Heap Memory Usage: " + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage());
        log.info("=================================================================\n");
    }
}
