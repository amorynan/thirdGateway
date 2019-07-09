package com.cjs.amory.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: amory
 * @Date: 2019-07-09
 */
public class PhoneUtil {

    public static String MATCH = "^1[3|4|5|7|8][0-9]\\d{4,8}$";

    public static boolean isValidPhoneNum(final String phoneNum){
        if (phoneNum == null || phoneNum.length() != 11){
            return false;
        }
        final Pattern pattern = Pattern.compile(MATCH);
        final Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()){
            return false;
        }
        return true;

    }
}
