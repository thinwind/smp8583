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
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import win.shangyh.cmnpro.smp8583.BodyField;
import win.shangyh.cmnpro.smp8583.BodyFieldType;

/**
 *
 * TODO VariableLengthFieldWorkerTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  13:42
 *
 */
public class VariableLengthFieldWorkerTest {

    @Test
    public void testParse() {
        VariableLengthFieldWorker worker = new VariableLengthFieldWorker(2,BodyFieldType.CHARACTOR);
        byte[] data = new byte[10];
        data[0] = '0';
        data[1] = '8';
        for(int i=2;i<data.length;i++){
            data[i] =(byte) ((int)'a'+i);
        }
        BodyField field = worker.parseField(data, 0, 19);
        assertEquals(19, field.getLocationIdx());
        assertEquals(10, field.getTotalLength());
        assertEquals("08cdefghij", field.toString());
        assertArrayEquals(data, field.getOrigin());
        assertFalse(data == field.getOrigin());
    }
}
