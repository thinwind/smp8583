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

import java.nio.charset.Charset;

/**
 *
 * 使用ASCII编码的基类
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  10:07
 *
 */
public abstract class BaseBodyFieldWorker implements BodyFieldWorker {
    public static final Charset ASCII_CHARSET = Charset.forName("ASCII");
    
    protected static String toAscii(byte[] data){
        return new String(data,ASCII_CHARSET);
    }
    
    protected static byte[] toByteArray(String asciiStr){
        return asciiStr.getBytes(ASCII_CHARSET);
    }
}
