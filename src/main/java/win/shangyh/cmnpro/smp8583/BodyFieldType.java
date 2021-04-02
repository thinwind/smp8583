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

import win.shangyh.cmnpro.smp8583.exception.IllegalLengthException;
import win.shangyh.cmnpro.smp8583.exception.UnsupportedFieldOprationException;

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
        @Override
        public byte[] normalize(byte[] origin, int length) {
            int delta = length - origin.length;
            if (delta < 0) {
                throw new IllegalLengthException("The input byte array has a larger length than expected.");
            }
            if (delta == 0) {
                return origin;
            }
            byte[] result = new byte[length];
            for (int i = 0; i < delta; i++)
                result[i] = '0';
            System.arraycopy(origin, 0, result, delta, origin.length);
            return result;
        }
    },
    CHARACTOR {

        @Override
        public byte[] normalize(byte[] origin, int length) {
            if (origin.length > length) {
                throw new IllegalLengthException("The input byte array has a larger length than expected.");
            }
            if (origin.length == length) {
                return origin;
            }
            byte[] r = new byte[length];
            System.arraycopy(origin, 0, r, 0, origin.length);
            for (int i = origin.length; i < length; i++) {
                r[i] = ' ';
            }
            return r;
        }
    },
    BYTES, BINARY;

    public byte[] normalize(byte[] origin, int length) {
        throw new UnsupportedFieldOprationException("The field cannot be normalized");
    }

}
