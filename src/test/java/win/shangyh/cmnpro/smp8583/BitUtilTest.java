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

    @Test(expected = RuntimeException.class)
    public void whenSizeLargerThanExpectedThenExcept() {
        BitUtil.splitInt(100, 2);
    }

    @Test
    public void whenSizeEqualsToExpectThenReturn() {
        byte[] r = BitUtil.splitInt(1234, 4);
        assertEquals(r.length, 4);
        assertEquals(new String(r, 0, 1), "1");
        assertEquals(new String(r, 1, 1), "2");
        assertEquals(new String(r, 2, 1), "3");
        assertEquals(new String(r, 3, 1), "4");
    }

    @Test
    public void whenSizeLessThanExpectedThenPaddingLeftWithZeros() {
        byte[] r = BitUtil.splitInt(1234, 8);
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
        byte[] cus=new byte[5];
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
            int p = ran.nextInt(64)+1;
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
        System.out.println(s);
        String o = "";
        for (int i = 0; i < bitmap.length; i++) {
            o += fill0To8(Integer.toBinaryString(bitmap[i] & 0xff));
        }
        System.out.println(o);
        assertEquals(s, o);
    }
    
    @Test
    public void testBitmapSet2() {
        byte[] bitmap = new byte[16];
        Set<Integer> pos = new TreeSet<>();
        Random ran = new Random();
        for (int i = 0; i < 20; i++) {
            int p = ran.nextInt(64)+1;
            pos.add(p);
            BitUtil.setPos(bitmap, p);
        }
        
        for (int i = 0; i < 20; i++) {
            int p = ran.nextInt(64)+65;
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
        System.out.println(s);
        String o = "";
        for (int i = 0; i < bitmap.length; i++) {
            o += fill0To8(Integer.toBinaryString(bitmap[i] & 0xff));
        }
        System.out.println(o);
        assertEquals(s, o);
    }

    private String fill0To8(String binaryString) {
        for (int i = 0, delta = 8 - binaryString.length(); i < delta; i++) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

}