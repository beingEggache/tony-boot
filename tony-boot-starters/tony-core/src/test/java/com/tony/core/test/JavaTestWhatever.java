package com.tony.core.test;

import java.util.regex.Pattern;

/**
 * JavaTestWhatever is
 *
 * @author tangli
 * @since 2023/07/20 11:06
 */
public class JavaTestWhatever {

    public static void main(String[] args) {
        String yyyyMMdd = "^[0-9]{4}(?:-[0-9]{2}){2}$";
        String yyyyMMddHHmmss = "^[0-9]{4}(?:-[0-9]{2}){2} [0-9]{2}(?::[0-9]{2}){2}$";
        String s1 = "2023-07-20";
        String s2 = "2023-07-20 11:03:00";

        boolean matches0 = Pattern.matches(yyyyMMdd, s1);
        boolean matches1 = Pattern.matches(yyyyMMdd, s2);
        boolean matches2 = Pattern.matches(yyyyMMddHHmmss, s1);
        boolean matches3 = Pattern.matches(yyyyMMddHHmmss, s2);
        System.out.println(matches0);
        System.out.println(matches1);
        System.out.println(matches2);
        System.out.println(matches3);
    }
}
