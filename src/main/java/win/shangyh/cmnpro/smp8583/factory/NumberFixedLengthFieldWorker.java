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

/**
 *
 * TODO NumberFixedLengthFieldWorker说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  10:58
 *
 */
public class NumberFixedLengthFieldWorker extends FixedLengthFieldWorker {

    private static final char PADDING_CHAR = '0';

    public NumberFixedLengthFieldWorker(int length) {
        super(length);
    }

    /**
     * 此方法仅在ascii长度小于length时调用
     */
    @Override
    protected String normalize(String ascii) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length - ascii.length(); i++) {
            builder.append(PADDING_CHAR);
        }
        builder.append(ascii);
        return builder.toString();
    }

}
