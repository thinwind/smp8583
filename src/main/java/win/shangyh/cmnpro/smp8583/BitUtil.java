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
package win.shangyh.cmnpro.smp8583;

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

    /**
     * 把一个数值使用ASCII码的字节数组表示
     * 
     * @param value 要拆分的数值
     * @param size 要拆分的字节个数
     * @return 拆分后的数组
     */
    public static byte[] splitInt(int value, int size) {
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

    public static String toAsciiString(byte[] data) {
        return new String(data, ASCII_CHARSET);
    }

    public static String toAsciiString(byte[] data, int start, int length) {
        return new String(data, start, length, ASCII_CHARSET);
    }

    public static String toHexString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            builder.append(Integer.toHexString(data[i] & 0xff));
        }
        return builder.toString();
    }

    public static byte[] toByteArray(String asciiStr) {
        return asciiStr.getBytes(ASCII_CHARSET);
    }

    /**
     * 设置bitmap某一位置存在
     * @param bitmap 要设置的bitmap
     * @param p 要设置的位置，从1开始计数
     */
    public static void setPos(byte[] bitmap, int p) {
        int idx=p-1;
        int segment = idx/8;
        int pos = idx % 8;
        int mask = 1 << 7;
        bitmap[segment] = (byte)(bitmap[segment] | (mask >>> pos));
    }
}
