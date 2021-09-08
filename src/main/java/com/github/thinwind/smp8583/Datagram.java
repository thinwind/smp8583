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
package com.github.thinwind.smp8583;

import com.github.thinwind.smp8583.factory.BytesDecoder;

/**
 *
 * 8583报文对象
 * 不带任何header
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  13:51
 *
 */
public class Datagram extends AbstractDatagram {

    @Override
    protected void parseHeader(byte[] source) {
        //just do nothing
    }
    
    @Override
    protected void parseHeader(byte[] source, BytesDecoder decoder) {
        //just do nothing
    }

    @Override
    public int getHeaderLength() {
        //no header
        return 0;
    }

    @Override
    protected void copyHeader(byte[] datagram) {
        //no header, just do nothing
    }

    @Override
    protected void setHeader(byte[] header) {
        //no header, just do nothing
    }

    @Override
    protected byte[] getHeader() {
        //no header, just return nothing
        return null;
    }

}
