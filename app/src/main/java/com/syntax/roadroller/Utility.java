package com.syntax.roadroller;

import java.util.Random;

public class Utility {
    public static String URL_PATTERN = "http://192.168.43.237:8080/RoadRollerServer1/Android/ServerController.jsp";

    public static String OTPVAL;
    public static String emailID="";

    public static char[] OTP(int len) {
        String numbers = "0123456789ABCD";
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }


}
