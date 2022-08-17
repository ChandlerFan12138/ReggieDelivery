package com.yusheng.reggie.utils;

import java.util.Random;

/**
 * 随机生成验证码工具类
 */
public class ValidateCodeUtils {
    /**
     * generate the code
     * @param length : 4 or 6 numbers
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;
        if(length == 4){
            code = new Random().nextInt(9999);//gengerate random numbers, max: 9999
            if(code < 1000){
                code = code + 1000;//make sure the number have 4 numbers
            }
        }else if(length == 6){
            code = new Random().nextInt(999999);//gengerate random numbers, max: 999999
            if(code < 100000){
                code = code + 100000;//make sure the number have 6 numbers
            }
        }else{
            throw new RuntimeException("only can generate the code with 4 or 6 numbers");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }
}
