package com.bloducspauter.base.utils;

import java.util.Arrays;



public class CreateVerificationCode {




    public static String getSecurityCode() {
        return getSecurityCode(4, false);
    }

    public static String getSecurityCode(int length, boolean isCanRepeat) {
        //除去容易混淆的0和o，1和l
        char[] codes = {
                '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                'j', 'm', 'n', 'p', 'q', 'r', 's', 't','u',
                'v', 'w', 'x', 'y', 'z','A','B','C','D','E',
                'F','G','H','J','K','L','M','N','P','Q','R','S',
                'T','U','V','W','X','Y','Z'};


        codes= Arrays.copyOfRange(codes,0,55);

        int n=codes.length;
        //抛出运行时异常
        if (length >n&& !isCanRepeat){
            throw new RuntimeException(
                    "调用securitycode.getSecurityCode(%1$s,len,level,isCanRepeat,n)");
        }
        char[] result=new char[length];
        //判断能否出现重复的字符
        if (isCanRepeat){
            for(int i=0;i<result.length;i++){
                //索引0 and n-1
                int r=(int)(Math.random()*n);
                //将result中的第i个元素设为codes[r]存放的数值
                result[i]=codes[r];
            }
        }else {
            for (int i=0;i<result.length;i++){
                int r=(int)(Math.random()*n);
                //将result中的第i个元素设为codes[r]存放的数值
                result[i]=codes[r];
                codes[r]=codes[n-1];
                n--;
            }
        }
        return String.valueOf(result);
    }
}
