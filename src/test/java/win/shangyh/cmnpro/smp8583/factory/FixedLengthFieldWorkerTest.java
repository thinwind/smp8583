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
    public void whenNumberAndLessLengthThenPaddingLeftWithZeros(){
        FixedLengthFieldWorker worker = new FixedLengthFieldWorker(6,BodyFieldType.NUMBER);
        byte[] bytes = new byte[4];
        bytes[0] = '1';
        bytes[1] = '2';
        bytes[2] = '3';
        bytes[3] = '4';
        BodyField bodyField = worker.createField(bytes, 3);
        
        byte[] target = new byte[6];
        target[0] = '0';
        target[1] = '0';
        target[2] = '1';
        target[3] = '2';
        target[4] = '3';
        target[5] = '4';
        assertArrayEquals(target, bodyField.getOrigin());
        assertEquals(3, bodyField.getLocation());
        assertEquals(6, bodyField.getTotalLength());
    }
    
    @Test
    public void whenNumberAndEqualsLengthThenReturn(){
        FixedLengthFieldWorker worker = new FixedLengthFieldWorker(4,BodyFieldType.NUMBER);
        byte[] bytes = new byte[4];
        bytes[0] = '1';
        bytes[1] = '2';
        bytes[2] = '3';
        bytes[3] = '4';
        BodyField bodyField = worker.createField(bytes, 3);

        assertArrayEquals(bytes, bodyField.getOrigin());
        assertEquals(3, bodyField.getLocation());
        assertEquals(4, bodyField.getTotalLength());
    }
    
    @Test
    public void testCreateNumberField(){
        FixedLengthFieldWorker worker = new FixedLengthFieldWorker(6,BodyFieldType.NUMBER);
        String ascii = "1234";
        BodyField bodyField = worker.createField(ascii, 3);
        
        byte[] bytes = new byte[6];
        bytes[0] = '0';
        bytes[1] = '0';
        bytes[2] = '1';
        bytes[3] = '2';
        bytes[4] = '3';
        bytes[5] = '4';
        
        assertArrayEquals(bytes, bodyField.getOrigin());
        assertEquals(3, bodyField.getLocation());
        assertEquals(6, bodyField.getTotalLength());
    }
    
    @Test
    public void testCreateCharField(){
        FixedLengthFieldWorker worker = new FixedLengthFieldWorker(9,BodyFieldType.CHARACTOR);
        String ascii = "abcd";
        BodyField bodyField = worker.createField(ascii, 117);
        byte[] bytes = new byte[9];
        bytes[0] = 'a';
        bytes[1] = 'b';
        bytes[2] = 'c';
        bytes[3] = 'd';
        bytes[4] = ' ';
        bytes[5] = ' ';
        bytes[6] = ' ';
        bytes[7] = ' ';
        bytes[8] = ' ';
        assertArrayEquals(bytes, bodyField.getOrigin());
        assertEquals(117, bodyField.getLocation());
        assertEquals(9, bodyField.getTotalLength());
    }
}
