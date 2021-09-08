/* 
 * Copyright 2021 Shang Yehua
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.thinwind.smp8583;

import java.nio.charset.Charset;

/**
 *
 * bit工具类
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  17:52
 *
 */
public final class BitUtil {

    private BitUtil() {
    }

    public static final Charset ASCII_CHARSET = Charset.forName("ASCII");

    public static final Charset GBK_CHARSET = Charset.forName("GBK");

    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    /**
     * 把一个数值使用ASCII码的字节数组表示
     * 
     * @param value 要拆分的数值
     * @param size 要拆分的字节个数
     * @return 拆分后的数组
     */
    public static byte[] splitIntInAscii(int value, int size) {
        String valStr = String.valueOf(value);
        int delta = size - valStr.length();
        if (delta < 0) {
            throw new RuntimeException(
                    String.format("The length([%s]) of value is larger than the expected size([%d]).", valStr, size));
        }
        for (int i = 0; i < delta; i++) {
            valStr = "0" + valStr;
        }
        return valStr.getBytes(ASCII_CHARSET);
    }

    /**
     * 将字节转为ASCII字符串
     * @param data 字节数组
     * @return ASCII编码的字符串
     */
    public static String toAsciiString(byte[] data) {
        return new String(data, ASCII_CHARSET);
    }

    /**
     * 将字节转为GBK字符串
     * @param data 字节数组
     * @return GBK编码的字符串
     */
    public static String toGBKString(byte[] data) {
        return new String(data, GBK_CHARSET);
    }

    /**
     * 将字节转为ASCII字符串
     * @param data 字节数组
     * @param start 要开始编码的字节起始位置
     * @param length 要进行编码的长度
     * @return ASCII编码的字符串
     */
    public static String toAsciiString(byte[] data, int start, int length) {
        return new String(data, start, length, ASCII_CHARSET);
    }

    /**
     * 将字节转为GBK字符串
     * @param data 字节数组
     * @param start 要开始编码的字节起始位置
     * @param length 要进行编码的长度
     * @return GBK编码的字符串
     */
    public static String toGBKString(byte[] data, int start, int length) {
        return new String(data, start, length, GBK_CHARSET);
    }

    /**
     * 将字节转为UTF-8字符串
     * @param data 字节数组
     * @param start 要开始编码的字节起始位置
     * @param length 要进行编码的长度
     * @return UTF-8编码的字符串
     */
    public static String toUtf8String(byte[] data, int start, int length) {
        return new String(data, start, length, UTF8_CHARSET);
    }

    /**
     * 将字符串编码成GBK对应的字节数组
     * 
     * @param data GBK字符串
     * @return GBK编码对应的字节数组
     */
    public static byte[] toGBKBytes(String data) {
        return data.getBytes(GBK_CHARSET);
    }

    /**
     * 将字符串编码成UTF8对应的字节数组
     * @param data UTF8编码的字符串
     * @return UTF8编码对应的字节数组
     */
    public static byte[] toUtf8Bytes(String data) {
        return data.getBytes(UTF8_CHARSET);
    }

    /**
     * 将字符数组编码成UTF8字符串
     * @param data 字节数组
     * @return UTF8编码对应的字符串
     */
    public static String toUtf8String(byte[] data) {
        return new String(data, UTF8_CHARSET);
    }

    public static String toHexString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            String hex = Integer.toHexString(data[i] & 0xff);
            builder.append(hex.length() == 2 ? hex : "0" + hex);
        }
        return builder.toString();
    }

    /**
     * 将字符串转成ASCII编码的字节数组
     * @param asciiStr ASCII编码的字符串
     * @return asciiStr对应的ASCII码字节数组
     */
    public static byte[] toByteArray(String asciiStr) {
        return asciiStr.getBytes(ASCII_CHARSET);
    }

    /**
     * 设置bitmap某一位置存在
     * 计算过程如下：
        //分段按位置计算
        //下面代码为消除局部变量，减少计算过程的实现
        int idx = p - 1;
        int segment = idx / 8;
        int pos = idx % 8;
        int mask = 1 << 7; //1000 0000
        bitmap[segment] = (byte) (bitmap[segment] | (mask >>> pos));
     * @param bitmap 要设置的bitmap
     * @param p 要设置的位置，从1开始计数
     */
    public static void setPos(byte[] bitmap, int p) {
        bitmap[(p - 1) / 8] = (byte) (bitmap[(p - 1) / 8] | (1 << (7 - ((p - 1) % 8))));
    }

    /**
     * 设置bitmap某一位置存在
     * @param bytes 字节序列
     * @param start bitmap的开始位置
     * @param p 要设置的域，从1开始计数
     */
    public static void setPos(byte[] bytes, int start, int p) {
        bytes[start + (p - 1) / 8] = (byte) (bytes[start + (p - 1) / 8] | (1 << (7 - ((p - 1) % 8))));
    }

    /**
     * 将一个数字拆分成byte数组
     * 效果最终相当于byte[]展开成一个大的数字
     * <p>
     * 计算过程:
     * 将最低位，赋值给数组的最末尾
     * 将最高位，赋值给数组的最开头
     * 
     * @param val 要拆分的数值
     * @param cnt 要拆分成字节数组的长度
     * @return 拆分后的数组
     */
    public static byte[] splitIntInBytes(int val, int cnt) {
        byte[] r = new byte[cnt];
        for (int i = cnt - 1; i >= 0; i--) {
            r[i] = (byte) ((val >>> (cnt - 1 - i) * 8) & 0xff);
        }
        return r;
    }

    /**
     * 将bytes合并成一个int数据
     * 效果相当于在内存中一个连续的字节段，表示的一个实际的整型值
     * 将开头赋值给高位
     * 将末尾赋值给低位
     * 以4个字节为例
     * bytes[0] bytes[1] bytes[2] bytes[3]
     * 相当于
     * bytes[0] 00000000 00000000 00000000
     * 00000000 bytes[1] 00000000 00000000
     * 00000000 00000000 bytes[2] 00000000
     * 00000000 00000000 00000000 bytes[3]
     * 
     * @param bytes
     * @return
     */
    public static int joinBytesToUnsignedInt(byte[] bytes) {
        int r = 0;
        for (int i = 0, cnt = bytes.length; i < cnt; i++) {
            int mask = 0xff << ((cnt - i - 1) * 8);
            r = r | ((bytes[i] << ((cnt - i - 1) * 8)) & mask);
        }
        return r;
    }

    /**
     * 将一个byte转为hex表示的字符串
     * 
     * 与Integer#toHexString不同，此方法不会省去前缀的0
     * @param b 要转换的byte
     * @return 2位的16进制全大写字符串
     */
    public static String byte2hex(byte b) {
        String hex = Integer.toHexString(b & 0xff);
        if (hex.length() < 2) {
            return "0" + hex.toUpperCase();
        } else {
            return hex.toUpperCase();
        }
    }
}
