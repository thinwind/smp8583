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

import com.github.thinwind.smp8583.exception.IllegalHeaderOrMtiException;
import com.github.thinwind.smp8583.factory.BytesDecoder;

/**
 *
 * 允许带header的8583报文
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-04  18:48
 *
 */
public class DatagramWithAsciiHeader extends AbstractDatagram {

    /**
     * 与CONNEX通信header
     */
    public static final int CONNEX_HEADER_LENGTH = 21;

    protected final int headerLength;

    private String header;

    public static DatagramWithAsciiHeader newConnexDatagram() {
        return new DatagramWithAsciiHeader(CONNEX_HEADER_LENGTH);
    }

    @Override
    protected void parseHeader(byte[] source) {
        header = BitUtil.toAsciiString(source, DATAGRAM_LENGTH_FIELD_SIZE, headerLength);
    }

    @Override
    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeader(String header) {
        if (BitUtil.toByteArray(header).length > headerLength) {
            throw new IllegalHeaderOrMtiException("Header length is larger than expected");
        }
        this.header = header;
    }

    @Override
    protected void copyHeader(byte[] datagram) {
        byte[] headerBytes = BitUtil.toByteArray(header);
        System.arraycopy(headerBytes, 0, datagram, DATAGRAM_LENGTH_FIELD_SIZE, headerLength);
    }

    public DatagramWithAsciiHeader(int headerLength) {
        this.headerLength = headerLength;
    }

    @Override
    protected void parseHeader(byte[] source, BytesDecoder decoder) {
        byte[] headerBytes = new byte[headerLength];
        System.arraycopy(source, DATAGRAM_LENGTH_FIELD_SIZE, headerBytes, 0, headerLength);
        header = decoder.decode(headerBytes);
    }

    @Override
    protected void setHeader(byte[] header) {
        this.header = BitUtil.toAsciiString(header);
    }

    @Override
    protected byte[] getHeader() {
        return BitUtil.toByteArray(header);
    }
    
    public String getHeaderStr(){
        return header;
    }

}
