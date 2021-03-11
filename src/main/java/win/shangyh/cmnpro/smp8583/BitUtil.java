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

import java.nio.charset.Charset;

/**
 *
 * bit工具类
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  17:52
 *
 */
public final class BitUtil {

    private BitUtil() {
    }

    public static final Charset ASCII_CHARSET = Charset.forName("ASCII");
    
    public static String toAsciiString(byte[] data) {
        return new String(data, ASCII_CHARSET);
    }
    
    public static String toAsciiString(byte[] data,int start,int length) {
        return new String(data,start,length,ASCII_CHARSET);
    }

    public static String toHexString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            builder.append(data[i] & 0xff);
        }
        return builder.toString();
    }
    
    public static byte[] toByteArray(String asciiStr){
        return asciiStr.getBytes(ASCII_CHARSET);
    }
}
