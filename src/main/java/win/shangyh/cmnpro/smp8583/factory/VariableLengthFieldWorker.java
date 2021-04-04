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
 * 生产可变长度域的打工人
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  12:07
 *
 */
public class VariableLengthFieldWorker implements BodyFieldWorker {

    private final int lengthFieldSize;

    private final BodyFieldType fieldType;

    private final int maxLength;

    public VariableLengthFieldWorker(int lengthFieldSize, int maxLength, BodyFieldType fieldType) {
        this.lengthFieldSize = lengthFieldSize;
        this.maxLength = maxLength;
        this.fieldType = fieldType;
    }

    @Override
    public BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx) {
        String lengthStr = BitUtil.toAsciiString(source, bodyOffset, lengthFieldSize);
        int fieldSize = Integer.parseInt(lengthStr);
        if (fieldSize > maxLength - lengthFieldSize) {
            throw new IllegalLengthException(
                    String.format("The value of the field has a larger length [%d] than the max length([%d]).",
                            fieldSize, maxLength - lengthFieldSize));
        }
        BodyField field = new BodyField();
        field.setLocation(bodyFieldIdx);
        field.setFieldType(fieldType);

        byte[] data = Arrays.copyOfRange(source, bodyOffset, bodyOffset + lengthFieldSize + fieldSize);
        field.setOrigin(data);
        return field;
    }

    @Override
    public BodyField createField(String ascii, int bodyFieldIdx) {
        byte[] body = BitUtil.toByteArray(ascii);
        return createField(body, bodyFieldIdx);
    }

    @Override
    public BodyField createField(byte[] body, int bodyFieldIdx) {
        if (body.length > maxLength) {
            throw new IllegalLengthException(
                    String.format("The value of the field has a larger length [%d] than the max length([%d]).",
                            body.length, maxLength));
        }
        BodyField field = new BodyField();
        field.setLocation(bodyFieldIdx);
        field.setFieldType(fieldType);

        byte[] lengthField = BitUtil.splitIntInAscii(body.length, lengthFieldSize);
        byte[] bodyData = new byte[lengthFieldSize + body.length];
        System.arraycopy(lengthField, 0, bodyData, 0, lengthFieldSize);
        System.arraycopy(body, 0, bodyData, lengthFieldSize, body.length);
        field.setOrigin(bodyData);

        return field;
    }

}
