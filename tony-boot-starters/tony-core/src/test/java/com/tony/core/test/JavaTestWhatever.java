package com.tony.core.test;

import com.tony.codec.CodecUtils;
import com.tony.codec.enums.Encoding;
import com.tony.utils.DigestUtils;
import com.tony.utils.StringUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * JavaTestWhatever is
 *
 * @author Tang Li
 * @date 2023/07/20 11:06
 */
public class JavaTestWhatever {

    public static void main(String[] args) {
        String src = "vcjizji3jo4jmfgd4390ducvjcxn34ngdlklsmcncxv9843jvn nsmnkev ewjkr23rvnkj文件尽快擦肩卡菲纳二五八";

        byte[] bytes = src.getBytes(StandardCharsets.UTF_8);

        String hex1 = new BigInteger(1, bytes).toString(16);
        System.out.println(hex1);
        System.out.println(CodecUtils.encodeToString(src,Encoding.HEX));

        byte[] md5s = DigestUtils.getDigest("MD5").digest(bytes);
        System.out.println(CodecUtils.encodeToString(md5s, Encoding.HEX));
        System.out.println(StringUtils.md5Uppercase(src));
    }
}
