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
        byte[] bitmap=new byte[16];
        String expected = "";
        assertEquals(expected, dgHex);
    }
}
