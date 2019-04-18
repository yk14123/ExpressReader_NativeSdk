package com.cnpeak.expressreader.utils;

import java.util.regex.Pattern;

/**
 * The type Regex utils.
 */
public class RegexUtils {

    private static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 验证是否为邮箱
     *
     * @param email the email
     * @return the boolean
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }


    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    private static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{8,12}$";

    public static boolean isPassword(String psd) {
        return Pattern.matches(REGEX_PASSWORD, psd);
    }


}
