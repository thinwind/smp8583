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

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import win.shangyh.cmnpro.smp8583.factory.BodyFieldFactory;

/**
 *
 * 报文体
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-03  14:18
 *
 */
public class DatagramBody {

    private byte[] bitmap;

    private int bodyOffset;

    /**
     * 第二位图是否存在的标识位
     * 主位图的第一个字节的第一位
     * -------------------------------------------
     * | flag | bm | bm | bm | bm | bm | bm | bm |
     * -------------------------------------------
     * 判断的方式:
     * 使用 1000 0000 (0x80) 判断
     * firstByte & 0x80 == 0x80
     * 可以判断是否flag位存在
     */
    private final static int SECONDARY_BITMAP_FLAG = 1 << 7;

    /**
     * 一个字节的后7位(0111 1111)
     */
    private final static int BYTE_LOW_SLOTS = (~SECONDARY_BITMAP_FLAG) & 0xff;

    /**
     * 每个字节槽位的标识值，用于判断每个byte某一位是否有值
     */
    private final static int[] BYTE_SLOTS = { 1 << 7, 1 << 6, 1 << 5, 1 << 4, 1 << 3, 1 << 2, 1 << 1, 1 };

    /**
     * 每个bitmap都是8字节(64位)
     */
    private final static int SINGLE_BITMAP_SIZE = 8;

    /**
     * 每个字节的长度,用于在bitmap中计算域的位置
     */
    private final static int SIMGLE_BITMAP_MAX_POS = SINGLE_BITMAP_SIZE * 8;

    @Setter
    @Getter
    private BodyField[] fields;

    public static DatagramBody fromBytes(byte[] source, int offset) {
        DatagramBody body = new DatagramBody();
        body.parse(source, offset);
        return body;
    }

    private void parse(byte[] source, int offset) {
        initBitmap(source, offset);
        parseBody(source);
    }

    private void parseBody(byte[] source) {
        //为了防止第一个bitmap的标识位的影响，把标识位暂时置空
        //由于bitmap已经初始化完成，此操作并不会有任何影响
        byte firstByte = bitmap[0];
        bitmap[0] = (byte) (firstByte & BYTE_LOW_SLOTS);

        fields = new BodyField[count(bitmap)];
        //域序号
        int bodyFieldIdx;
        int fieldCursor = 0;

        for (int i = 0; i < bitmap.length; i++) {
            byte item = bitmap[i];
            for (int slotIdx = 0; slotIdx < BYTE_SLOTS.length; slotIdx++) {
                //域位置索引
                // bodyFieldIdx = i * BYTE_LENGTH + slotIdx + 1;
                //判断某一域是否存在
                if ((item & BYTE_SLOTS[slotIdx]) == BYTE_SLOTS[slotIdx]) {
                    BodyField field = BodyFieldFactory.parseField(source, bodyOffset, bodyFieldIdx);
                    fields[fieldCursor++] = field;
                    bodyOffset += field.getTotalLength();
                }
            }
        }
        //将首字节还原
        bitmap[0] = firstByte;
    }

    /**
     * 计算一共有多少个域
     * 
     * @param bitmap 位图
     * @return 位图中1的个数
     */
    private static int count(byte[] bitmap) {
        int cnt = 0;
        for (byte b : bitmap) {
            cnt += Integer.bitCount(b & 0xff);
        }
        return cnt;
    }

    /*
     * 初始化bitmap
     * 记录body的起始位置
     */
    private void initBitmap(byte[] source, int offset) {
        byte firstByte = source[offset];
        bodyOffset = offset + SINGLE_BITMAP_SIZE;
        //判断第二位图是否存在
        boolean hasSecondaryBitmap = (firstByte & SECONDARY_BITMAP_FLAG) == SECONDARY_BITMAP_FLAG;
        if (hasSecondaryBitmap) {
            bodyOffset += SINGLE_BITMAP_SIZE;
        }
        bitmap = Arrays.copyOfRange(source, offset, bodyOffset);
    }

    /**
     * 所有的域拼接成byte数组，并且头部预留出
     * MTI和LENGTH的位置
     */
    public byte[] toBytes(int prefixSize) {
        boolean hasSecondaryBitmap = false;
        int totalBodyLength = 0;
        for (BodyField field : fields) {
            totalBodyLength += field.getTotalLength();
            if (!hasSecondaryBitmap && (field.getLocation() > SIMGLE_BITMAP_MAX_POS)) {
                //如果存在大于一个bitmap的域索引，那么证明第二个bitmap存在
                hasSecondaryBitmap = true;
            }
        }

        if (hasSecondaryBitmap) {
            bitmap = new byte[SINGLE_BITMAP_SIZE * 2];
        } else {
            bitmap = new byte[SINGLE_BITMAP_SIZE];
        }

        int totalLength = prefixSize + bitmap.length + totalBodyLength;
        byte[] datagram = new byte[totalLength];
        
        //copy bitmap
        System.arraycopy(bitmap, 0, datagram, prefixSize, bitmap.length);
        bodyOffset = prefixSize+bitmap.length;
        
        // copyFields
        for (BodyField field : fields) {
            System.arraycopy(field.getOrigin(), 0, datagram, bodyOffset, field.getTotalLength());
            bodyOffset += field.getTotalLength();
        }
        
        return datagram;
    }

}
