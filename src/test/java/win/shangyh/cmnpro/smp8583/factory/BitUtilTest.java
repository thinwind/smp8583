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
package win.shangyh.cmnpro.smp8583.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import win.shangyh.cmnpro.smp8583.BitUtil;

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
            bytes[i / 2] = (byte)Integer.parseInt(asciiInHex.substring(i, i+2), 16);
        }
        assertEquals(expected, BitUtil.toAsciiString(bytes));
    }
    
    @Test
    public void testToHexString(){
        String asciiInHex = "20212324303132334041425e";
        String str = " !#$0123@AB^";
        byte[] bytes = BitUtil.toByteArray(str);
        assertEquals(asciiInHex, BitUtil.toHexString(bytes));
    }
}
