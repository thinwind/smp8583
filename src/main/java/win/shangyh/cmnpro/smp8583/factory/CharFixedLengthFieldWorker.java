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

import win.shangyh.cmnpro.smp8583.BodyFieldType;

/**
 *
 * 字符型固定长度域
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  11:23
 *
 */
public class CharFixedLengthFieldWorker extends FixedLengthFieldWorker {

    private static final char PADDING_CHAR = ' ';
    
    public CharFixedLengthFieldWorker(int length) {
        super(length);
    }

    @Override
    protected String normalize(String ascii) {
        StringBuilder builder = new StringBuilder();
        builder.append(ascii);
        for (int i = 0; i < length - ascii.length(); i++) {
            builder.append(PADDING_CHAR);
        }
        return builder.toString();
    }

    @Override
    protected BodyFieldType getFieldType() {
        return BodyFieldType.CHARACTOR;
    }
    
}
