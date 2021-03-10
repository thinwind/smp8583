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

import java.util.Random;

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

    /**
     * 计算一个byte中包含1的个数
     * 
     * {@Link https://zh.wikipedia.org/wiki/%E6%B1%89%E6%98%8E%E9%87%8D%E9%87%8F}
     * {@Link https://en.wikipedia.org/wiki/Hamming_weightF}
     * @see Integer#bitCount(int)
     * @param b 要计算的byte的数值
     * @return byte包含的1的个数
     */
    public static int bitCount(byte b) {
        int i = b & 0xff;
        i = i - ((i >>> 1) & 0x55);
        i = (i & 0x33) + ((i >>> 2) & 0x33);
        return (i & 0x0f) + ((i >>> 4) & 0x0f);
    }

    /**
     * 计算一个byte数组中所有的1的个数
     * 
     * @param bytes 要计算的数组
     * @return byte数组中所有的1的个数
     */
    public static int bitCount(byte[] bytes) {
        int cnt = 0;
        int i;
        for (int s = 0; s < bytes.length; s++) {
            i = bytes[s] & 0xff;
            i = i - ((i >>> 1) & 0x55);
            i = (i & 0x33) + ((i >>> 2) & 0x33);
            cnt += (i & 0x0f) + ((i >>> 4) & 0x0f);
        }
        return cnt;
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[Integer.MAX_VALUE/2];
        fill(bytes);
        int c = 0;
        long s1 = System.nanoTime();
        int n = bitCount(bytes);
        long s2 = System.nanoTime();
        for (byte b : bytes) {
            c += Integer.bitCount(b & 0xff);
        }
        long s3 = System.nanoTime();
        System.out.println((c - n) + ":\t" + (s2 - s1)/1000000 + "\t" + (s3 - s2)/1000000);
        // for(int i=1;i<=Byte.MAX_VALUE;i++){
        //     byte[] bytes=new byte[i];
        //     fill(bytes);
        //     int c = 0;
        //     long s1 = System.nanoTime();
        //     for (byte b : bytes) {
        //         c+= Integer.bitCount(b);
        //     }
        //     long s2 = System.nanoTime();
        //     int n = bitCount(bytes);
        //     long s3 = System.nanoTime();
        //     System.out.println(c+":"+n+"\t"+(c-n));
        // }
    }

    private static void fill(byte[] bytes) {
        Random random = new Random();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) random.nextInt(10000);
        }
    }
}
