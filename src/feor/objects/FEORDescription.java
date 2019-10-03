//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor.objects;

import java.util.Objects;
import java.util.Map;

public class FEORDescription
{
    private final String text;
    private Map<String, Object> perspectives;
    
    public FEORDescription(final String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof FEORDescription) {
            final FEORDescription otherFD = (FEORDescription)obj;
            return otherFD.getText().equals(this.getText());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.text);
        return hash;
    }
    
    @Override
    public String toString() {
        return "FEORDescription{text=" + this.text + '}';
    }
}
