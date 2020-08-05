package com.demo.common.util.date;

/**
 * @Author thymi
 * @Date 2020/7/15
 */
public enum DateFormat {

    /**
     * 日期格式, yyyy-MM-dd
     */
    DATE_DASH("yyyy-MM-dd"),

    /**
     * 日期时间格式, 破折号: yyyy-MM-dd HH:mm:ss
     */
    DATETIME_DASH("yyyy-MM-dd HH:mm:ss"),

    /**
     * 日期格式, 点: yyyy.MM.dd
     */
    DATE_DOT("yyyy.MM.dd"),

    /**
     * 日期时间格式, 点: yyyy.MM.dd HH:mm:ss
     */
    DATETIME_DOT("yyyy.MM.dd HH:mm:ss"),

    /**
     * 日期时间格式, 斜线: yyyy/MM/dd HH:mm:ss
     */
    DATE_SLASH("yyyy/MM/dd"),

    /**
     * 日期时间格式, 斜线: yyyy/MM/dd HH:mm:ss
     */
    DATETIME_SLASH("yyyy/MM/dd HH:mm:ss"),

    /**
     * 分隔符,空格: " "
     */
    SPILT_SPACE(" "),

    /**
     * 分隔符,点: "."
     */
    SPILT_DOT("."),

    /**
     * 分隔符,破折号: "-"
     */
    SPILT_DASH("-"),

    /**
     * 分隔符,斜线: "/"
     */
    SPILT_SLASH("/");

    private final String value;

    DateFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * 将datetime格式转换为同类的date格式, 如: "DATETIME_DASH" to "DATE_DASH"
     *
     * @return
     */
    public DateFormat toDate() {
        if (this.compareTo(DATETIME_DASH) == 0) {
            return DATE_DASH;
        }
        if (this.compareTo(DATETIME_DOT) == 0) {
            return DATE_DOT;
        }
        if(this.compareTo(DATETIME_SLASH) == 0){
            return DATE_SLASH;
        }
        return this;
    }
}
