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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import win.shangyh.cmnpro.smp8583.factory.BodyFieldFactory;

/**
 *
 * TODO DatagramBodyTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-02  16:51
 *
 */
public class DatagramBodyTest {

    @Test
    public void whenToBytesThenContainJustBodyAndBitmap() {
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

        DatagramBody body = new DatagramBody();
        body.setFields(fields);
        byte[] datagram = body.toBytes(6);
        String dgHex = BitUtil.toHexString(datagram);
        
        List<Byte> byteList=new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            byteList.add((byte)0x00);
        }
        
        //bitmap
        byte[] bitmap=new byte[16];
        BitUtil.setPos(bitmap, 1);
        BitUtil.setPos(bitmap, 2);
        BitUtil.setPos(bitmap, 32);
        BitUtil.setPos(bitmap, 37);
        BitUtil.setPos(bitmap, 61);
        BitUtil.setPos(bitmap, 91);
        BitUtil.setPos(bitmap, 101);
        BitUtil.setPos(bitmap, 117);
        addAll(byteList,bitmap);
        
        //fields
        //2
        String card="6226581125000087";
        byte[] cardBytes=BitUtil.toByteArray(card);
        addAll(byteList, BitUtil.splitIntInAscii(card.length(), 2));
        addAll(byteList, cardBytes);
        
        //32
        String f32="406251";
        addAll(byteList, BitUtil.splitIntInAscii(f32.length(), 2));
        addAll(byteList, BitUtil.toByteArray(f32));
        
        //37
        String f37="381469901408";
        for (int i = 0,delta=12-f37.length(); i < delta; i++) {
            f37 = f37+" ";
        }
        addAll(byteList, BitUtil.toByteArray(f37));
        
        //61
        String f61="CEBV1 111111111       Y";
        addAll(byteList, BitUtil.splitIntInAscii(f61.length(), 3));
        addAll(byteList, BitUtil.toByteArray(f61));
        
        //91
        String f91="2";
        addAll(byteList, BitUtil.toByteArray(f91));
        
        //101
        String f101="CARDFILE";
        addAll(byteList, BitUtil.splitIntInAscii(f101.length(), 2));
        addAll(byteList, BitUtil.toByteArray(f101));
        
        //117
        String f117="00";
        addAll(byteList, BitUtil.splitIntInAscii(f117.length(), 3));
        addAll(byteList, BitUtil.toByteArray(f117));
        
        byte[] target = new byte[byteList.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = byteList.get(i);
        }
        String expected = BitUtil.toHexString(target);
        System.out.println(expected);
        System.out.println(dgHex);
        assertEquals(expected, dgHex);
    }

    private void addAll(List<Byte> byteList, byte[] bitmap) {
        for (byte b : bitmap) {
            byteList.add(b);
        }
    }
}
