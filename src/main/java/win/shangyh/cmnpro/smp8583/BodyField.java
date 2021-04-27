package win.shangyh.cmnpro.smp8583;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BodyField implements Comparable<BodyField>{

    /**
     * 域号，从1开始计数
     */
    private int location;

    private byte[] origin;

    private BodyFieldType fieldType;

    public int getTotalLength() {
        return origin.length;
    }

    public String toHexString() {
        return BitUtil.toHexString(origin);
    }

    @Override
    public String toString() {
        return String.format("BodyField\n[type=%s\nlocation=%d\ndata=%s]", fieldType, location,
                BitUtil.toHexString(origin));
    }

    @Override
    public int compareTo(BodyField o) {
        return this.location - o.location;
    }

}
