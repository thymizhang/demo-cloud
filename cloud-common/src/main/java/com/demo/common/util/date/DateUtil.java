package com.demo.common.util.date;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;

/**
 * 日期时间工具类
 *
 * @author thymi
 */
public class DateUtil {

    public static DateTimeFormatter FORMATTER_DATE_DASH = DateTimeFormatter.ofPattern(DateFormat.DATE_DASH.getValue());

    public static DateTimeFormatter FORMATTER_DATETIME_DASH = DateTimeFormatter.ofPattern(DateFormat.DATETIME_DASH.getValue());

    public static DateTimeFormatter FORMATTER_DATE_DOT = DateTimeFormatter.ofPattern(DateFormat.DATE_DOT.getValue());

    public static DateTimeFormatter FORMATTER_DATETIME_DOT = DateTimeFormatter.ofPattern(DateFormat.DATETIME_DOT.getValue());

    public static DateTimeFormatter FORMATTER_DATE_SLASH = DateTimeFormatter.ofPattern(DateFormat.DATE_SLASH.getValue());

    public static DateTimeFormatter FORMATTER_DATETIME_SLASH = DateTimeFormatter.ofPattern(DateFormat.DATETIME_SLASH.getValue());

    /**
     * 日期转换为标准格式 "2020-10-10"
     *
     * @param date 日期字符串, 格式: "2020.10.10" or "2020/10/10"
     * @return "2020-10-10"
     */
    public static String toStandard(String date) {
        date = date.trim().replace(".", "-").replace("/", "-");
        if (!date.contains(DateFormat.SPILT_SPACE.getValue())) {
            date = date + " 00:00:00";
        }
        return date;
    }

    /**
     * 日期格式化
     *
     * @param date    日期字符串, 格式: "2020-10-10" or "2020-10-10 18:18:18", 默认: 当前时间
     * @param format  格式化类型, 默认: "2020-10-10 18:18:18"
     * @param hasTime 返回格式是否包含时间, false: 输出"2020-10-10"
     * @return
     */
    public static String format(String date, DateFormat format, boolean hasTime) {
        if (format == null) {
            format = DateFormat.DATETIME_DASH;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format.getValue());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format.toDate().getValue());

        LocalDateTime localDateTime = DateUtil.stringToLocalDateTime(date);

        if (hasTime) {
            // 输出时间
            return localDateTime.format(dateTimeFormatter);
        } else {
            // 不输出时间
            return localDateTime.toLocalDate().format(dateFormatter);
        }
    }

    /**
     * 日期格式化, 默认格式: 2020-10-10 18:18:18
     *
     * @param date    日期字符串
     * @param hasTime 返回格式是否包含时间, false: 输出"2020-10-10"
     * @return
     */
    public static String format(String date, boolean hasTime) {
        return DateUtil.format(date, DateFormat.DATETIME_DASH, hasTime);
    }

    /**
     * 获取日期中的日期值
     *
     * @param date 日期字符串, 格式: "2020-10-10" or "2020-10-10 18:18:18"
     * @return 天
     */
    public static int getDay(String date) {
        LocalDateTime localDateTime = DateUtil.stringToLocalDateTime(date);
        int compare = localDateTime.compareTo(localDateTime.with(TemporalAdjusters.firstDayOfMonth()));
        return compare + 1;
    }

    /**
     * 获取日期中的月份
     *
     * @param date 日期字符串, 格式: "2020-10-10" or "2020-10-10 18:18:18"
     * @return 月份
     */
    public static int getMonth(String date) {
        return DateUtil.stringToLocalDate(date).getMonthValue();
    }

    /**
     * 获取日期中的年份
     *
     * @param date 日期字符串, 格式: "2020-10-10" or "2020-10-10 18:18:18"
     * @return 年份
     */
    public static int getYear(String date) {
        return DateUtil.stringToLocalDate(date).getYear();
    }

    /**
     * 获取日期最早的时间
     *
     * @param dateTime 日期字符串, 格式: "2020-10-10" or "2020-10-10 18:18:18"
     * @return "2020-10-10 00:00:00"
     */
    public static String firstTimeOfDay(String dateTime) {
        dateTime = DateUtil.toStandard(dateTime);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateUtil.FORMATTER_DATETIME_DASH);
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN).format(DateUtil.FORMATTER_DATETIME_DASH);
    }

    /**
     * 获取当天最晚的时间
     *
     * @param date 日期字符串, 格式: "2020-10-10" or "2020-10-10 18:18:18"
     * @return "2020-10-10 23:59:59"
     */
    public static String lastTimeOfDay(String date) {
        LocalDate localDate = DateUtil.stringToLocalDate(date);
        return LocalDateTime.of(localDate, LocalTime.MAX).format(DateUtil.FORMATTER_DATETIME_DASH);
    }

    /**
     * 获取指定日期月份的第一天日期
     *
     * @param date 指定日期, 格式: 2020-10-10
     * @return 该月第一天日期
     */
    public static String firstDayOfMonth(String date) {
        LocalDate lastDate = DateUtil.stringToLocalDate(date).with(TemporalAdjusters.firstDayOfMonth());
        return lastDate.format(DateUtil.FORMATTER_DATE_DASH);
    }

    /**
     * 获取指定日期月份的最后一天日期
     *
     * @param date 指定日期, 格式: 2020-10-10
     * @return 该月最后一天日期
     */
    public static String lastDayOfMonth(String date) {
        LocalDate lastDate = DateUtil.stringToLocalDate(date).with(TemporalAdjusters.lastDayOfMonth());
        return lastDate.format(DateUtil.FORMATTER_DATE_DASH);
    }

    /**
     * 获取日期中年份的第一天
     *
     * @param date 日期字符串, 格式: 2020-10-10
     * @return 年第一天
     */
    public static String firstDayOfYear(String date) {
        LocalDate localDate = DateUtil.stringToLocalDate(date);
        return localDate.with(TemporalAdjusters.firstDayOfYear()).toString();
    }

    /**
     * 获取日期中年份的最后一天
     *
     * @param date 日期字符串, 格式: 2020-10-10
     * @return 年最后一天
     */
    public static String lastDayOfYear(String date) {
        LocalDate localDate = DateUtil.stringToLocalDate(date);
        return localDate.with(TemporalAdjusters.lastDayOfYear()).toString();
    }

    /**
     * 获取当前日期在一年中的自然周
     *
     * @param date 字符串日期, 格式: 2020-10-10
     * @return 自然周
     */
    public static int weekOfYear(String date) {
        LocalDate localDate = DateUtil.stringToLocalDate(date);
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        return localDate.get(weekFields.weekOfYear());
    }

    /**
     * 获取当前日期是星期几
     *
     * @param date 日期字符串, 格式: 2020-10-10
     * @return
     */
    public static int weekOfDate(String date) {
        LocalDate localDate = DateUtil.stringToLocalDate(date);
        return localDate.get(WeekFields.of(DayOfWeek.of(1), 1).dayOfWeek());
    }

    /**
     * 为指定日期增加天数
     *
     * @param date      日期字符串, 格式: 2020-10-10
     * @param daysToAdd 增加天数
     * @return 增加天数后的日期
     */
    public static String plusDay(String date, int daysToAdd) {
        LocalDate localDate = DateUtil.stringToLocalDate(date).plusDays(daysToAdd);
        return localDate.format(DateUtil.FORMATTER_DATE_DASH);
    }

    /**
     * 为指定日期增加月数
     *
     * @param date        字符串日期, 格式: 2020-10-10
     * @param monthsToAdd 增加的月数
     * @return 增加月数后的日期
     */
    public static String plusMonth(String date, int monthsToAdd) {
        LocalDate localDate = DateUtil.stringToLocalDate(date).plusMonths(monthsToAdd);
        return localDate.format(DateUtil.FORMATTER_DATE_DASH);
    }

    /**
     * 判断日期是否早于当前日期
     * 使用场景: 判断有效期是否过期,比如服务是否有效、计划是否到期
     *
     * @param date 需要比较的日期,格式"2020-10-10"
     * @return true:比较日期早于当前日期
     */
    public static boolean isBeforeNow(String date) {
        LocalDate parse = LocalDate.parse(date, DateUtil.FORMATTER_DATE_DASH);
        LocalDate now = LocalDate.now();
        return parse.isBefore(now);
    }

    /**
     * 比较日期是否早于目标日期
     *
     * @param targetDate  目标日期, 格式: 2020-10-10
     * @param compareDate 比较日期, 格式: 2020-10-10
     * @return true: 比较日期早于目标日期
     */
    public static boolean isBefore(String targetDate, String compareDate) {
        return DateUtil.isCompare(targetDate, compareDate, "before");
    }

    /**
     * 判断日期是否晚于当前日期<br/>
     * 使用场景: 判断有效期是否过期,比如服务是否有效、计划是否到期
     *
     * @param date 需要比较的日期,格式"2020-10-10"
     * @return true:比较日期早于当前日期
     */
    public static boolean isAfterNow(String date) {
        LocalDate parse = DateUtil.stringToLocalDate(date);
        LocalDate now = LocalDate.now();
        return parse.isAfter(now);
    }

    /**
     * 判断比较日期是否晚于目标日期
     *
     * @param targetDate  目标日期, 格式: 2020-10-10
     * @param compareDate 比较日期, 格式: 2020-10-10
     * @return true: 比较日期晚于目标日期
     */
    public static boolean isAfter(String targetDate, String compareDate) {
        return DateUtil.isCompare(targetDate, compareDate, "after");
    }

    /**
     * 比较日期早于或晚于目标日期
     *
     * @param targetDate  目标日期, 格式: 2020-10-10
     * @param compareDate 比较日期, 格式: 2020-10-10
     * @param type        比较类型, "before": 早于, "after": 晚于
     * @return true: 比较日期早于或晚于目标日期
     */
    public static boolean isCompare(String targetDate, String compareDate, String type) {
        LocalDate targetLocalDate = DateUtil.stringToLocalDate(targetDate);
        LocalDate compareLocalDate = DateUtil.stringToLocalDate(compareDate);

        String before = "before", after = "after";
        if (type.equals(before)) {
            return compareLocalDate.isBefore(targetLocalDate);
        }
        if (type.equals(after)) {
            return compareLocalDate.isAfter(targetLocalDate);
        }
        // 如果没有类型匹配,默认"before"
        return compareLocalDate.isBefore(targetLocalDate);
    }

    /**
     * 日期字符串转换成为LocalDate对象
     *
     * @param date 日期字符串, 格式: "2020-10-10" or "2020-10-10 17:00:00"
     * @return LocalDate
     */
    public static LocalDate stringToLocalDate(String date) {
        return DateUtil.stringToLocalDateTime(date).toLocalDate();
    }

    /**
     * 日期字符串转换成为LocalDateTime对象
     *
     * @param dateTime 日期字符串, 格式: "2020-10-10" or "2020-10-10 17:00:00"
     * @return LocalDateTime
     */
    public static LocalDateTime stringToLocalDateTime(String dateTime) {
        if (dateTime == null || dateTime.trim().equals("")) {
            dateTime = DateUtil.now();
        }
        return LocalDateTime.parse(DateUtil.toStandard(dateTime.trim()), DateUtil.FORMATTER_DATETIME_DASH);
    }

    /**
     * 将Java日期对象转换为LocalDateTime对象
     *
     * @param date Java日期对象(java.util.Date)
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 将Java日期对象转换为日期时间字符串
     *
     * @param date Java日期对象(java.util.Date)
     * @return 日期时间字符串, 格式: 2020-10-10 18:18:18
     */
    public static String dateToString(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(DateUtil.FORMATTER_DATETIME_DASH);
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param startDate 开始日期, 日期格式: "2020-10-10" or "2020-10-10 18:18:18"
     * @param endDate   截止日期, 日期格式: "2020-10-10" or "2020-10-10 18:18:18"
     * @return 日期差
     */
    public static long dateDiff(String startDate, String endDate) {
        LocalDate ldtStart = DateUtil.stringToLocalDate(startDate);
        LocalDate ldtEnd = DateUtil.stringToLocalDate(endDate);
        return ldtEnd.toEpochDay() - ldtStart.toEpochDay();
    }


    /**
     * 取当前系统日期时间
     *
     * @param dateFormat 日期时间格式
     * @return
     */
    public static String now(DateFormat dateFormat) {
        DateTimeFormatter dateTimeFormatter = (
                dateFormat == null ?
                        DateUtil.FORMATTER_DATETIME_DASH :
                        DateTimeFormatter.ofPattern(dateFormat.getValue())
        );
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    /**
     * 获取当前系统时间
     *
     * @return 格式: "2020-10-10 18:18:18"
     */
    public static String now() {
        return DateUtil.now(null);
    }

    public static void main(String[] args) throws Exception {
        String d1 = "2020-12-15";
        String d2 = "2020/08/11 17:00:00";

        Long start = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        System.out.println(DateUtil.firstDayOfYear(d1));
        System.out.println(DateUtil.lastDayOfYear(d1));

        Long end = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println("执行时间(ms): " + (end - start));
    }
}

