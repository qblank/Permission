package cn.qblank.util;

import java.util.Date;
import java.util.Random;

/**
 * @author evan_qb
 * @date 2018/8/24 15:54
 */

public class PasswordUtil {
    public final static String[] word = {
            "a","b","c","d","e","f","g","h",
            "j","k","m","n","o","p","q","r",
            "s","t","u","v","w","x","y","z",
            "A","B","C","D","E","F","G","H",
            "J","K","M","N","O","P","Q","R",
            "S","T","U","V","W","X","Y","Z"
    };

    public final static String[] num = {
            "2","3","3","5","6","7","8","9"
    };

    public static String randomPassword(){
        StringBuffer sb = new StringBuffer();
        Random random = new Random(new Date().getTime());
        boolean flag = false;
        int len = random.nextInt(3) + 8;
        for (int i = 0; i < len; i++) {
            if (flag){
                sb.append(num[random.nextInt(num.length)]);
            }else{
                sb.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(PasswordUtil.randomPassword());
        System.out.println(PasswordUtil.randomPassword());
        System.out.println(PasswordUtil.randomPassword());
    }
}
