package com.github.thinwind.smp8583;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BodyField implements Comparable<BodyField> {

    /**
     * 域号，从1开始计数
     */
    private int location;

    private byte[] origin;
    
    private int lengthFieldSize;

    private BodyFieldType fieldType;

    public int getTotalLength() {
        return origin.length;
    }

    public String toHexString() {
        return BitUtil.toHexString(origin);
    }
    
    public String toValueString(Charset charset){
        return new String(origin,lengthFieldSize,origin.length-lengthFieldSize,charset);
    }
    
    @Override
    public String toString() {
        return String.format("BodyField%n[type=%s%nlocation=%d%ndata=%s]", fieldType, location,
                BitUtil.toHexString(origin));
    }

    @Override
    /**
     * 支持排序，按照location排序
     */
    public int compareTo(BodyField o) {
        return this.location - o.location;
    }

}
