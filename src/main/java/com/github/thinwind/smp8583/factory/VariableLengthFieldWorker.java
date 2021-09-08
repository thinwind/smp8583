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
package com.github.thinwind.smp8583.factory;

import java.util.Arrays;

import com.github.thinwind.smp8583.BitUtil;
import com.github.thinwind.smp8583.BodyField;
import com.github.thinwind.smp8583.BodyFieldType;
import com.github.thinwind.smp8583.exception.IllegalLengthException;

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
        if (fieldSize > maxLength) {
            throw new IllegalLengthException(
                    String.format("The value of the field has a larger length [%d] than the max length([%d]).",
                            fieldSize, maxLength - lengthFieldSize));
        }
        byte[] data = Arrays.copyOfRange(source, bodyOffset, bodyOffset + lengthFieldSize + fieldSize);
        return internalParse(bodyFieldIdx, data);
    }

    @Override
    public BodyField parseField(byte[] source, int bodyOffset, int bodyFieldIdx, BytesDecoder decoder) {
        byte[] inputLength = new byte[lengthFieldSize];
        System.arraycopy(source, bodyOffset, inputLength, 0, lengthFieldSize);
        String lengthStr = decoder.decode(inputLength);
        int fieldSize = Integer.parseInt(lengthStr);
        if (fieldSize > maxLength) {
            throw new IllegalLengthException(
                    String.format("The value of the field has a larger length [%d] than the max length([%d]).",
                            fieldSize, maxLength - lengthFieldSize));
        }
        byte[] inputData = Arrays.copyOfRange(source, bodyOffset, bodyOffset + lengthFieldSize + fieldSize);
        byte[] data = BitUtil.toGBKBytes(decoder.decode(inputData));

        return internalParse(bodyFieldIdx, data);
    }

    private BodyField internalParse(int bodyFieldIdx, byte[] data) {
        BodyField field = new BodyField();
        field.setLocation(bodyFieldIdx);
        field.setFieldType(fieldType);
        field.setOrigin(data);
        field.setLengthFieldSize(lengthFieldSize);
        return field;
    }

    @Override
    public BodyField createField(String text, int bodyFieldIdx) {
        byte[] body = BitUtil.toGBKBytes(text);
        return createField(body, bodyFieldIdx);
    }

    @Override
    public BodyField createField(byte[] body, int bodyFieldIdx) {
        byte[] lengthField = BitUtil.splitIntInAscii(body.length, lengthFieldSize);
        return merge(lengthField, body, bodyFieldIdx);
    }

    @Override
    public BodyField createField(String text, int bodyFieldIdx, boolean valueToEbcdic, BytesEncoder encoder) {
        byte[] body;
        if (valueToEbcdic) {
            body = encoder.encode(text);
        } else {
            body = BitUtil.toGBKBytes(text);
        }
        String valStr = String.valueOf(body.length);
        int delta = lengthFieldSize - valStr.length();
        if (delta < 0) {
            throw new RuntimeException(String.format(
                    "The length([%s]) of value is larger than the expected size([%d]).", valStr, lengthFieldSize));
        }
        for (int i = 0; i < delta; i++) {
            valStr = "0" + valStr;
        }
        byte[] lengthField = encoder.encode(valStr);
        return merge(lengthField, body, bodyFieldIdx);
    }

    private BodyField merge(byte[] lengthField, byte[] body, int bodyFieldIdx) {
        if (body.length > maxLength + lengthFieldSize) {
            throw new IllegalLengthException(
                    String.format("The value of the field has a larger length [%d] than the max length([%d]).",
                            body.length, maxLength));
        }
        BodyField field = new BodyField();
        field.setLocation(bodyFieldIdx);
        field.setFieldType(fieldType);
        byte[] bodyData = new byte[lengthFieldSize + body.length];
        System.arraycopy(lengthField, 0, bodyData, 0, lengthFieldSize);
        System.arraycopy(body, 0, bodyData, lengthFieldSize, body.length);
        field.setOrigin(bodyData);
        field.setLengthFieldSize(lengthFieldSize);
        return field;
    }

}
