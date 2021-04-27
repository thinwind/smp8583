/* 
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
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
import java.util.Set;
import java.util.TreeSet;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * TODO BitUtilTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-02  10:38
 *
 */
public class BitUtilTest {
    
    @Test
    public void testToByteArray(){
        String mti="0302";
        byte[] mtiInBytes = new byte[4];
        mtiInBytes[0] = '0';
        mtiInBytes[1] = '3';
        mtiInBytes[2] = '0';
        mtiInBytes[3] = '2';
        assertArrayEquals(mtiInBytes, BitUtil.toByteArray(mti));
    }

    @Test(expected = RuntimeException.class)
    public void whenSizeLargerThanExpectedThenExcept() {
        BitUtil.splitIntInAscii(100, 2);
    }

    @Test
    public void whenSizeEqualsToExpectThenReturn() {
        byte[] r = BitUtil.splitIntInAscii(1234, 4);
        assertEquals(r.length, 4);
        assertEquals(new String(r, 0, 1), "1");
        assertEquals(new String(r, 1, 1), "2");
        assertEquals(new String(r, 2, 1), "3");
        assertEquals(new String(r, 3, 1), "4");
    }

    @Test
    public void whenSizeLessThanExpectedThenPaddingLeftWithZeros() {
        byte[] r = BitUtil.splitIntInAscii(1234, 8);
        assertEquals(8, r.length);
        assertEquals(new String(r, 0, 1), "0");
        assertEquals(new String(r, 1, 1), "0");
        assertEquals(new String(r, 2, 1), "0");
        assertEquals(new String(r, 3, 1), "0");
        assertEquals(new String(r, 4, 1), "1");
        assertEquals(new String(r, 5, 1), "2");
        assertEquals(new String(r, 6, 1), "3");
        assertEquals(new String(r, 7, 1), "4");
    }

    @Test
    public void testToAscii() {
        String asciiInHex = "20212324303132334041425e";
        String expected = " !#$0123@AB^";
        byte[] bytes = new byte[asciiInHex.length() / 2];
        for (int i = 0; i < asciiInHex.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(asciiInHex.substring(i, i + 2), 16);
        }
        assertEquals(expected, BitUtil.toAsciiString(bytes));
    }

    @Test
    public void testAsciiToHexString() {
        String asciiInHex = "20212324303132334041425e";
        String str = " !#$0123@AB^";
        byte[] bytes = BitUtil.toByteArray(str);
        assertEquals(asciiInHex, BitUtil.toHexString(bytes));
    }

    @Test
    public void testToHexStringWithZeros() {
        byte[] cus = new byte[5];
        cus[0] = 0x00;
        cus[1] = 0x01;
        cus[2] = 0x03;
        cus[3] = (byte) 0xCA;
        cus[4] = (byte) 0xFE;
        String expected = "000103cafe";
        assertEquals(expected, BitUtil.toHexString(cus));
    }

    @Test
    public void testBitmapSet() {
        byte[] bitmap = new byte[8];
        Set<Integer> pos = new TreeSet<>();
        Random ran = new Random();
        for (int i = 0; i < 20; i++) {
            int p = ran.nextInt(64) + 1;
            pos.add(p);
            BitUtil.setPos(bitmap, p);
        }
        String s = "";
        for (int i = 1; i < 65; i++) {
            if (pos.contains(i)) {
                s += "1";
            } else {
                s += "0";
            }
        }
        String o = "";
        for (int i = 0; i < bitmap.length; i++) {
            o += fill0To8(Integer.toBinaryString(bitmap[i] & 0xff));
        }
        assertEquals(s, o);
    }

    @Test
    public void testBitmapSet2() {
        byte[] bitmap = new byte[16];
        Set<Integer> pos = new TreeSet<>();
        Random ran = new Random();
        for (int i = 0; i < 20; i++) {
            int p = ran.nextInt(64) + 1;
            pos.add(p);
            BitUtil.setPos(bitmap, p);
        }

        for (int i = 0; i < 20; i++) {
            int p = ran.nextInt(64) + 65;
            pos.add(p);
            BitUtil.setPos(bitmap, p);
        }

        String s = "";
        for (int i = 1; i < 129; i++) {
            if (pos.contains(i)) {
                s += "1";
            } else {
                s += "0";
            }
        }
        String o = "";
        for (int i = 0; i < bitmap.length; i++) {
            o += fill0To8(Integer.toBinaryString(bitmap[i] & 0xff));
        }
        assertEquals(s, o);
    }

    private String fill0To8(String binaryString) {
        for (int i = 0, delta = 8 - binaryString.length(); i < delta; i++) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

    @Test
    public void splitIntInBytesTest() {
        int s = new Random().nextInt(65535);
        String b = Integer.toBinaryString(s);
        byte[] target = new byte[2];
        target[0] = (byte) Integer.parseInt(b.substring(0, b.length() - 8), 2);
        target[1] = (byte) Integer.parseInt(b.substring(b.length() - 8, b.length()), 2);
        int t = Integer.parseInt(BitUtil.toHexString(BitUtil.splitIntInBytes(s, 2)), 16);
        assertArrayEquals(target, BitUtil.splitIntInBytes(s, 2));
        assertEquals(s, t);
    }

    @Test
    public void splitIntInBytesTest4() {
        int s = new Random().nextInt(Integer.MAX_VALUE / 2) + Short.MAX_VALUE;
        String b = Integer.toBinaryString(s);
        byte[] target = new byte[4];
        target[0] = (byte) Integer.parseInt(b.substring(0, b.length() - 24), 2);
        target[1] = (byte) Integer.parseInt(b.substring(b.length() - 24, b.length() - 16), 2);
        target[2] = (byte) Integer.parseInt(b.substring(b.length() - 16, b.length() - 8), 2);
        target[3] = (byte) Integer.parseInt(b.substring(b.length() - 8, b.length()), 2);
        int t = Integer.parseInt(BitUtil.toHexString(BitUtil.splitIntInBytes(s, 4)), 16);
        assertArrayEquals(target, BitUtil.splitIntInBytes(s, 4));
        assertEquals(s, t);
    }

    @Test
    public void joinBytesToIntTest() {
        for (int i = 0; i < 10000; i++) {
            int s = new Random().nextInt(65535);
            byte[] splited = BitUtil.splitIntInBytes(s, 2);
            int t = BitUtil.joinBytesToUnsignedInt(splited);
            assertEquals(s, t);
        }
    }
    
    @Test
    public void testGbkString() throws UnsupportedEncodingException{
        String ch = "中文";
        assertEquals("中文", BitUtil.toGBKString(ch.getBytes("GBK")));
    }
    
    @Test
    public void testGbkBytes() throws UnsupportedEncodingException{
        String ch = "中文";
        assertArrayEquals("中文".getBytes("GBK"), BitUtil.toGBKBytes(ch));
    }
    
    @Test
    public void testUtf8Encode() throws UnsupportedEncodingException{
        String ch = "中文";
        assertArrayEquals("中文".getBytes("UTF-8"), BitUtil.toUtf8Bytes(ch));
    }
    
    @Test
    public void testUtf8Decode() throws UnsupportedEncodingException{
        String ch = "中文";
        assertEquals("中文", BitUtil.toUtf8String(ch.getBytes("UTF-8")));
    }
    
    @Test
    public void testUtf8Decode2() throws UnsupportedEncodingException{
        String ch = "中文";
        byte[] array = new byte[10];
        System.arraycopy(ch.getBytes("UTF-8"), 0, array, 3, 6);
        assertEquals("中文", BitUtil.toUtf8String(array,3,6));
    }
}
