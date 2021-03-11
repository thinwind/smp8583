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

/**
 *
 * 报文域的类型
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  14:11
 *
 */
public enum BodyFieldType {
    NUMBER {
        private static final char PADDING_CHAR = '0';

        @Override
        public String defaultString(byte[] data) {
            return BitUtil.toAsciiString(data);
        }

        @Override
        public String normalize(String ascii, int length) {
            if (ascii == null || (ascii.length() >= length)) {
                return ascii;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length - ascii.length(); i++) {
                builder.append(PADDING_CHAR);
            }
            builder.append(ascii);
            return builder.toString();
        }
    },
    CHARACTOR {

        private static final char PADDING_CHAR = ' ';

        @Override
        public String defaultString(byte[] data) {
            return BitUtil.toAsciiString(data);
        }

        @Override
        public String normalize(String ascii, int length) {
            if (ascii == null || (ascii.length() >= length)) {
                return String.valueOf(ascii);
            }
            StringBuilder builder = new StringBuilder();
            builder.append(ascii);
            for (int i = 0; i < length - ascii.length(); i++) {
                builder.append(PADDING_CHAR);
            }
            return builder.toString();
        }
    },
    BINARY {
        @Override
        public String defaultString(byte[] data) {
            return BitUtil.toHexString(data);
        }

        @Override
        public String normalize(String ascii, int length) {
            return String.valueOf(ascii);
        }
    };

    public abstract String defaultString(byte[] data);

    public abstract String normalize(String ascii, int length);

}
