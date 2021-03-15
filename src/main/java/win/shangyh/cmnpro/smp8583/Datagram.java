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
package win.shangyh.cmnpro.smp8583;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 8583报文对象
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  13:51
 *
 */
@Setter
@Getter
public class Datagram {

    public final static int DATAGRAM_LENGTH = 2;

    //mti长度
    private final static int MTI_LENGTH = 4;

    private String mti;

    private DatagramBody body;

    public void parse(byte[] source) {
        mti = BitUtil.toAsciiString(source, DATAGRAM_LENGTH, MTI_LENGTH);
        body = DatagramBody.fromBytes(source, DATAGRAM_LENGTH + MTI_LENGTH);
    }

    public byte[] toBytes() {
        return body.toBytes(BitUtil.toByteArray(mti));
    }
}
