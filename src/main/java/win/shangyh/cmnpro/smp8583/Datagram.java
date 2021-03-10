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

import java.io.UnsupportedEncodingException;

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
    //mti长度
    private final static int MTI_LENGTH=4;
    
    private static final String CHARSET = "ASCII";
    
    private String mti;

    private DatagramBody body;
    
    public void parse(byte[] source){
        try {
            mti = new String(source,0,MTI_LENGTH,CHARSET);
        } catch (UnsupportedEncodingException e) {
            //只要充分测试，此异常不应该出现
            throw new RuntimeException(e);
        }
        body = DatagramBody.fromBytes(source,MTI_LENGTH);
    }
}
