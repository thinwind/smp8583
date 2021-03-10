package win.shangyh.cmnpro.smp8583;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BodyField {
    
    private int locationIdx;
    
    private String asciiStr;
    
    private byte[] origin;
    
    public int getTotalLength(){
        return origin.length;
    }
    
    public String asAscii(){
        return asciiStr;
    }
    
    @Override
    public String toString(){
        return asciiStr;
    }
    
}
