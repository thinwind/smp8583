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

/**
 *
 * TODO NumberFixedLengthFieldWorkerTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  11:14
 *
 */
public class NumberFixedLengthFieldWorkerTest {
    
    @Test
    public void testCreateField(){
        NumberFixedLengthFieldWorker worker = new NumberFixedLengthFieldWorker(6);
        String ascii = "1234";
        BodyField bodyField = worker.createField(ascii, 3);
        assertEquals("001234", bodyField.asAscii());
        byte[] bytes = new byte[6];
        bytes[0] = (byte)'0';
        bytes[1] = (byte)'0';
        bytes[2] = (byte)'1';
        bytes[3] = (byte)'2';
        bytes[4] = (byte)'3';
        bytes[5] = (byte)'4';
        assertArrayEquals(bytes, bodyField.getOrigin());
        assertEquals(3, bodyField.getLocationIdx());
        assertEquals(6, bodyField.getTotalLength());
    }
}
