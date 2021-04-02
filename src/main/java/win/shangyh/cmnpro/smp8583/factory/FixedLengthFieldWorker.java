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

import java.util.Arrays;

import win.shangyh.cmnpro.smp8583.BitUtil;
import win.shangyh.cmnpro.smp8583.BodyField;
import win.shangyh.cmnpro.smp8583.BodyFieldType;
import win.shangyh.cmnpro.smp8583.exception.IllegalLengthException;

/**
 *
 * 生产固定长度域的打工人
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  09:56
 *
 */
public class FixedLengthFieldWorker implements BodyFieldWorker {

    protected final int length;

    private final BodyFieldType fieldType;
    
    public FixedLengthFieldWorker(int length, BodyFieldType fieldType) {
        this.length = length;
        this.fieldType = fieldType;
    }

    @Override
    public BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx) {
        BodyField field = new BodyField();
        field.setLocation(bodyFieldIdx);
        byte[] fieldDg = Arrays.copyOfRange(source, bodyOffset, bodyOffset + length);
        field.setOrigin(fieldDg);
        return field;
    }

    @Override
    public BodyField createField(String ascii, int bodyFieldIdx) {
        return createField(BitUtil.toByteArray(ascii), bodyFieldIdx);
    }


    @Override
    public BodyField createField(byte[] data, int bodyFieldIdx) {
        if (data.length > length) {
            throw new IllegalLengthException(String.format(
                    "The value of the field has a larger length [%d] than expected([%d]).", data.length, length));
        }

        BodyField field = new BodyField();
        field.setLocation(bodyFieldIdx);
        field.setFieldType(fieldType);
        field.setOrigin(fieldType.normalize(data, length));

        return field;
    }

}
