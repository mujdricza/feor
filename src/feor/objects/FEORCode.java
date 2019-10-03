//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor.objects;

public class FEORCode extends FEORDescription
{
    private final Integer codeInt;
    
    public FEORCode(final String text) {
        super(text);
        this.codeInt = Integer.valueOf(text);
    }
    
    public Integer getCodeInteger() {
        return this.codeInt;
    }
    
    public String getCodeString() {
        return super.getText();
    }
    
    @Override
    public String toString() {
        return "FEORCode{codeInt=" + this.codeInt + '}';
    }
}
