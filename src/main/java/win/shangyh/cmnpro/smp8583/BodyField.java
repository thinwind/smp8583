package win.shangyh.cmnpro.smp8583;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BodyField {
    
    /**
     * 域号，从1开始计数
     */
    private int location;
    
    private byte[] origin;
    
    private BodyFieldType fieldType;
    
    public int getTotalLength(){
        return origin.length;
    }
    
    @Override
    public String toString(){
        return fieldType.defaultString(origin);
    }
    
    public String toHexString(){
        return BitUtil.toHexString(origin);
    }
    
}
