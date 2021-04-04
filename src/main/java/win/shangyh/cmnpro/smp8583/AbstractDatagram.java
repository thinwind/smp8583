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

import lombok.Getter;
import lombok.Setter;

/**
 *
 * AbstractDatagram报文基类
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-04  18:50
 *
 */
@Setter
@Getter
public abstract class AbstractDatagram {
    /**
     * 报文长度域长度
     */
    public final static int DATAGRAM_LENGTH_FIELD_SIZE = 2;

    //mti长度
    public final static int MTI_LENGTH = 4;

    protected String mti;

    protected DatagramBody body;
    
    public void parse(byte[] source) {
        int headerLength = getHeaderLength();
        mti = BitUtil.toAsciiString(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength, MTI_LENGTH);
        parseHeader(source);
        body = DatagramBody.fromBytes(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength + MTI_LENGTH);
    }

    protected abstract void parseHeader(byte[] source);

    public abstract int getHeaderLength();

    public byte[] toBytes() {
        int headerLength = getHeaderLength();
        byte[] datagram = body.toBytes(DATAGRAM_LENGTH_FIELD_SIZE + MTI_LENGTH + headerLength);

        byte[] lengthBytes = BitUtil.splitIntInBytes(datagram.length, DATAGRAM_LENGTH_FIELD_SIZE);
        System.arraycopy(lengthBytes, 0, datagram, 0, DATAGRAM_LENGTH_FIELD_SIZE);
        
        copyHeader(datagram);

        byte[] mtiBytes = BitUtil.toByteArray(mti);
        System.arraycopy(mtiBytes, 0, datagram, DATAGRAM_LENGTH_FIELD_SIZE+headerLength, mtiBytes.length);

        return datagram;
    }

    protected abstract void copyHeader(byte[] datagram);
}
