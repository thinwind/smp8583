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
package com.github.thinwind.smp8583;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import com.github.thinwind.smp8583.factory.BodyFieldFactory;
import org.junit.Test;

/**
 *
 * TODO DatagramTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-04  17:38
 *
 */
public class DatagramTest {
    
    @Test
    public void parseTest(){
        BodyField[] fields = new BodyField[7];
        //1100 0000 0000 0000 0000 0000 0000 0001
        //0000 0000 1000 0000 0000 0000 0000 1000
        //0000 0000 0000 0000 0000 0000 0010 0000
        //0000 1000 0000 0000 0000 1000 0000 0000
        fields[0] = BodyFieldFactory.createBodyField("6226581125000087", 2);
        fields[1] = BodyFieldFactory.createBodyField("406251", 32);
        fields[2] = BodyFieldFactory.createBodyField("381469901408", 37);
        fields[3] = BodyFieldFactory.createBodyField("CEBV1 111111111       Y", 61);
        fields[4] = BodyFieldFactory.createBodyField("2", 91);
        fields[5] = BodyFieldFactory.createBodyField("CARDFILE", 101);
        fields[6] = BodyFieldFactory.createBodyField("00", 117);

        DatagramBody source = new DatagramBody();
        source.setFields(fields);
        Datagram dg=new Datagram();
        dg.setMti("0302");
        dg.setBody(source);
        
        byte[] datagram = dg.toBytes();
        Datagram parsedDg = new Datagram();
        parsedDg.parse(datagram);
        
        byte[] mtiInBytes = new byte[4];
        mtiInBytes[0] = '0';
        mtiInBytes[1] = '3';
        mtiInBytes[2] = '0';
        mtiInBytes[3] = '2';
        assertArrayEquals(mtiInBytes, parsedDg.mti);
        
        byte[] lengthBytes=new byte[2];
        lengthBytes[0] = datagram[0];
        lengthBytes[1] = datagram[1];
        
        assertEquals(datagram.length, BitUtil.joinBytesToUnsignedInt(lengthBytes));
        
        DatagramBody target = parsedDg.getBody();
        assertEquals(7, target.getFields().length);
        BodyField[] parsedFields = target.getFields();
        for (int i = 0; i < parsedFields.length; i++) {
            assertEquals(fields[i].getLocation(), parsedFields[i].getLocation());
            assertEquals(fields[i].getFieldType(), parsedFields[i].getFieldType());
            assertArrayEquals(fields[i].getOrigin(), parsedFields[i].getOrigin());
        }
        
        assertArrayEquals(dg.toBytes(), parsedDg.toBytes());
    }
    
    @Test
    public void whenMtiInBytesThenCopyDirectly(){
        BodyField[] fields = new BodyField[7];
        //1100 0000 0000 0000 0000 0000 0000 0001
        //0000 0000 1000 0000 0000 0000 0000 1000
        //0000 0000 0000 0000 0000 0000 0010 0000
        //0000 1000 0000 0000 0000 1000 0000 0000
        fields[0] = BodyFieldFactory.createBodyField("6226581125000087", 2);
        fields[1] = BodyFieldFactory.createBodyField("406251", 32);
        fields[2] = BodyFieldFactory.createBodyField("381469901408", 37);
        fields[3] = BodyFieldFactory.createBodyField("CEBV1 111111111       Y", 61);
        fields[4] = BodyFieldFactory.createBodyField("2", 91);
        fields[5] = BodyFieldFactory.createBodyField("CARDFILE", 101);
        fields[6] = BodyFieldFactory.createBodyField("00", 117);

        DatagramBody source = new DatagramBody();
        source.setFields(fields);
        Datagram dg=new Datagram();
        dg.setMti(new byte[]{(byte)0xf0,(byte)0xf1,(byte)0xf1,(byte) 0xf0});
        dg.setBody(source);
        
        byte[] datagram = dg.toBytes();
        Datagram parsedDg = new Datagram();
        parsedDg.parse(datagram);
        
        assertArrayEquals(new byte[]{(byte)0xf0,(byte)0xf1,(byte)0xf1,(byte) 0xf0}, parsedDg.getMti());
        
        byte[] lengthBytes=new byte[2];
        lengthBytes[0] = datagram[0];
        lengthBytes[1] = datagram[1];
        
        assertEquals(datagram.length, BitUtil.joinBytesToUnsignedInt(lengthBytes));
        
        DatagramBody target = parsedDg.getBody();
        assertEquals(7, target.getFields().length);
        BodyField[] parsedFields = target.getFields();
        for (int i = 0; i < parsedFields.length; i++) {
            assertEquals(fields[i].getLocation(), parsedFields[i].getLocation());
            assertEquals(fields[i].getFieldType(), parsedFields[i].getFieldType());
            assertArrayEquals(fields[i].getOrigin(), parsedFields[i].getOrigin());
        }
        
        assertArrayEquals(dg.toBytes(), parsedDg.toBytes());
    }
}
