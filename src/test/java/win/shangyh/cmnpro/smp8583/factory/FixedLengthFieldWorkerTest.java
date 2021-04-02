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
package win.shangyh.cmnpro.smp8583.factory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import win.shangyh.cmnpro.smp8583.BodyField;
import win.shangyh.cmnpro.smp8583.BodyFieldType;

/**
 *
 * TODO NumberFixedLengthFieldWorkerTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  11:14
 *
 */
public class FixedLengthFieldWorkerTest {
    
    @Test
    public void testCreateNumberField(){
        FixedLengthFieldWorker worker = new FixedLengthFieldWorker(6,BodyFieldType.NUMBER);
        String ascii = "1234";
        BodyField bodyField = worker.createField(ascii, 3);
        assertEquals("001234", bodyField.toString());
        byte[] bytes = new byte[6];
        bytes[0] = (byte)'0';
        bytes[1] = (byte)'0';
        bytes[2] = (byte)'1';
        bytes[3] = (byte)'2';
        bytes[4] = (byte)'3';
        bytes[5] = (byte)'4';
        assertArrayEquals(bytes, bodyField.getOrigin());
        assertEquals(3, bodyField.getLocation());
        assertEquals(6, bodyField.getTotalLength());
    }
    
    @Test
    public void testCreateCharField(){
        FixedLengthFieldWorker worker = new FixedLengthFieldWorker(9,BodyFieldType.CHARACTOR);
        String ascii = "abcd";
        BodyField bodyField = worker.createField(ascii, 117);
        assertEquals("abcd     ", bodyField.toString());
        byte[] bytes = new byte[9];
        bytes[0] = (byte)'a';
        bytes[1] = (byte)'b';
        bytes[2] = (byte)'c';
        bytes[3] = (byte)'d';
        bytes[4] = (byte)' ';
        bytes[5] = (byte)' ';
        bytes[6] = (byte)' ';
        bytes[7] = (byte)' ';
        bytes[8] = (byte)' ';
        assertArrayEquals(bytes, bodyField.getOrigin());
        assertEquals(117, bodyField.getLocation());
        assertEquals(9, bodyField.getTotalLength());
    }
}
