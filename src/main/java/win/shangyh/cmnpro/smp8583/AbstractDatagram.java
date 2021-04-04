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
    
    /**
     * 解析报文
     * 将报文拆分为MTI和DatagramBody
     * DatagramBody将报文拆分为 BodyField，可以获取各域的byte数组或者ascii表示
     * 
     * @param source 报文体
     */
    public void parse(byte[] source) {
        int headerLength = getHeaderLength();
        mti = BitUtil.toAsciiString(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength, MTI_LENGTH);
        parseHeader(source);
        body = DatagramBody.fromBytes(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength + MTI_LENGTH);
    }

    /**
     * 解析header
     * 在解析报文过程调用，如果不支持header
     * 不用进行任何操作即可
     * 
     * @param source 原始报文体
     */
    protected abstract void parseHeader(byte[] source);

    /**
     * 获取header的长度
     * 可以为0，长度为0时，认为是不支持header
     * 
     * @return header的长度
     */
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

    /**
     * 将 header 字节copy到最终的报文中
     * 
     * @param datagram 最终生成的报文
     */
    protected abstract void copyHeader(byte[] datagram);
}
