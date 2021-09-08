/* 
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
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
package com.github.thinwind.smp8583;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import com.github.thinwind.smp8583.exception.IllegalLengthException;
import com.github.thinwind.smp8583.factory.BytesDecoder;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * AbstractDatagram报文基类
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-04-04  18:50
 *
 */
@Setter
@Getter
public abstract class AbstractDatagram {
    /**
     * 报文长度域长度
     */
    public final static int DATAGRAM_LENGTH_FIELD_SIZE = 2;

    //mti长度
    public final static int MTI_LENGTH = 4;

    protected byte[] mti;

    protected DatagramBody body;

    protected String key;

    /**
     * 解析报文
     * 将报文拆分为MTI和DatagramBody
     * DatagramBody将报文拆分为 BodyField，可以获取各域的byte数组或者ascii表示
     * 
     * @param source 报文体
     */
    public void parse(byte[] source) {
        int headerLength = getHeaderLength();
        mti = new byte[MTI_LENGTH];
        System.arraycopy(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength, mti, 0, MTI_LENGTH);
        parseHeader(source);
        body = DatagramBody.fromBytes(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength + MTI_LENGTH);
    }

    public void parse(byte[] source, BytesDecoder decoder) {
        int headerLength = getHeaderLength();
        byte[] inputMti = new byte[MTI_LENGTH];
        System.arraycopy(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength, inputMti, 0, MTI_LENGTH);

        String inputMtiUtf = decoder.decode(inputMti);
        mti = inputMtiUtf.getBytes(BitUtil.ASCII_CHARSET);
        parseHeader(source, decoder);

        body = DatagramBody.fromBytes(source, DATAGRAM_LENGTH_FIELD_SIZE + headerLength + MTI_LENGTH, decoder);
    }

    /**
     * 解析header，并使用decoder进行解码
     * 在解析报文过程调用，如果不支持header
     * 不用进行任何操作即可
     * 
     * @param source 原始报文体
     */
    protected abstract void parseHeader(byte[] source, BytesDecoder decoder);
    /**
     * 解析header
     * 在解析报文过程调用，如果不支持header
     * 不用进行任何操作即可
     * 
     * @param source 原始报文体
     */
    protected abstract void parseHeader(byte[] source);
    
    /**
     * 设置header
     * 在解析报文过程调用，如果不支持header
     * 不用进行任何操作即可
     * 
     * @param source 原始报文体
     */
    protected abstract void setHeader(byte[] header);
    
    /**
     * 获取header
     * 在解析报文过程调用，如果不支持header
     * 不用进行任何操作即可
     * 
     * @param source 原始报文体
     */
    protected abstract byte[] getHeader();

    /**
     * 获取header的长度
     * 可以为0，长度为0时，认为是不支持header
     * 
     * @return header的长度
     */
    public abstract int getHeaderLength();

    /**
     * 将报文对象转成byte数组
     * 
     * 在默认实现中，此方法会遍历所有的域
     * 将所有的域按照顺序拼接成一个长的报文数组
     * 
     * 注意：在默认实现过程中，解析时不会存储
     * 原始的报文，所以每次调用此方法都是重新
     * 生成一次全新的报文数组
     * 
     * 如果需要重复使用原始的报文数组，可以编写
     * 写的报文对象，重写 AbstractDatagram#parse
     * 和此方法
     * 
     * @return 报文对象对应的数组
     */
    public byte[] toBytes() {
        int headerLength = getHeaderLength();
        byte[] datagram = body.toBytes(DATAGRAM_LENGTH_FIELD_SIZE + MTI_LENGTH + headerLength);

        byte[] lengthBytes = BitUtil.splitIntInBytes(datagram.length, DATAGRAM_LENGTH_FIELD_SIZE);
        System.arraycopy(lengthBytes, 0, datagram, 0, DATAGRAM_LENGTH_FIELD_SIZE);

        copyHeader(datagram);

        System.arraycopy(mti, 0, datagram, DATAGRAM_LENGTH_FIELD_SIZE + headerLength, MTI_LENGTH);

        return datagram;
    }

    /**
     * 将 header 字节copy到最终的报文中
     * 
     * @param datagram 最终生成的报文
     */
    protected abstract void copyHeader(byte[] datagram);

    public byte[] getFieldInBytes(int i) {
        for (BodyField field : body.getFields()) {
            if (field.getLocation() == i) {
                return field.getOrigin();
            }
        }
        return null;
    }

    public BodyField getBodyField(int location) {
        for (BodyField field : body.getFields()) {
            if (field.getLocation() == location) {
                return field;
            }
        }
        return null;
    }

    public void setMti(String mti) {
        this.mti = BitUtil.toByteArray(mti);
        if (this.mti.length > MTI_LENGTH)
            throw new IllegalLengthException("MTI length is not equals to " + MTI_LENGTH);
    }

    public void setMti(byte[] mti) {
        this.mti = mti;
        if (this.mti.length > MTI_LENGTH)
            throw new IllegalLengthException("MTI length is not equals to " + MTI_LENGTH);
    }

    public boolean hasSecondaryBitmap() {
        return body.hasSecondaryBitmap();
    }

    public String getMtiAsString() {
        return BitUtil.toAsciiString(mti);
    }
    
    public <T extends AbstractDatagram> T removeField(Supplier<T> supplier,int... locations) {
        T result = supplier.get();
        result.mti = this.mti;
        result.setHeader(this.getHeader());
        DatagramBody body = new DatagramBody();
        List<BodyField> filterdFields = new ArrayList<>();
        for (BodyField bodyField : this.body.getFields()) {
            boolean removed = false;
            for (int loc : locations) {
                if (loc == bodyField.getLocation()) {
                    removed = true;
                    break;
                }
            }
            if (!removed) {
                filterdFields.add(bodyField);
            }
        }

        body.setFields(filterdFields.toArray(new BodyField[filterdFields.size()]));
        result.setBody(body);
        return result;
    }

    public <T extends AbstractDatagram> T addOrReplaceField(Supplier<T> supplier,BodyField filed) {
        if (exists(filed.getLocation())) {
            return replaceField(supplier,filed);
        } else {
            return addField(supplier,filed);
        }
    }

    private boolean exists(int location) {
        for (BodyField b : this.body.getFields()) {
            if (b.getLocation() == location)
                return true;
        }
        return false;
    }

    private <T extends AbstractDatagram> T replaceField(Supplier<T> supplier,BodyField filed) {
        T result = supplier.get();
        result.mti = this.mti;
        result.setHeader(this.getHeader());
        DatagramBody body = new DatagramBody();
        BodyField[] orignFields = this.body.getFields();
        BodyField[] extendedFields = new BodyField[orignFields.length];
        for (int i = 0; i < extendedFields.length; i++) {
            if (filed.getLocation() == orignFields[i].getLocation()) {
                extendedFields[i] = filed;
            } else {
                extendedFields[i] = orignFields[i];
            }
        }
        body.setFields(extendedFields);
        result.setBody(body);
        return result;
    }

    private <T extends AbstractDatagram> T addField(Supplier<T> supplier,BodyField field) {
        T result = supplier.get();
        result.mti = this.mti;
        result.setHeader(this.getHeader());
        DatagramBody body = new DatagramBody();
        BodyField[] orignFields = this.body.getFields();
        BodyField[] extendedFields = new BodyField[orignFields.length + 1];
        //边界情况，有可能添加到最后一个域了
        if (field.getLocation() > orignFields[orignFields.length - 1].getLocation()) {
            System.arraycopy(orignFields, 0, extendedFields, 0, orignFields.length);
            extendedFields[extendedFields.length - 1] = field;
        } else {
            for (int i = 0, j = 0; i < extendedFields.length; i++, j++) {
                if (i == j && field.getLocation() < orignFields[j].getLocation()) {
                    extendedFields[i] = field;
                    j--;
                } else {
                    extendedFields[i] = orignFields[j];
                }
            }
        }

        body.setFields(extendedFields);
        result.setBody(body);
        return result;
    }

    public <T extends AbstractDatagram> T addOrReplaceFields(Supplier<T> supplier,BodyField... fileds) {
        T result = supplier.get();
        result.mti = this.mti;
        DatagramBody body = new DatagramBody();
        BodyField[] orignFields = this.body.getFields();
        Map<Integer, BodyField> cache = new TreeMap<>();
        for (BodyField bodyField : orignFields) {
            cache.put(bodyField.getLocation(), bodyField);
        }
        for (BodyField field : fileds) {
            cache.put(field.getLocation(), field);
        }
        BodyField[] extendedFields = new BodyField[cache.size()];

        int idx = 0;
        for (BodyField value : cache.values()) {
            extendedFields[idx++] = value;
        }

        body.setFields(extendedFields);
        result.setBody(body);
        return result;
    }
}
