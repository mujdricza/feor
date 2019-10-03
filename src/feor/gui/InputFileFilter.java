//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor.gui;

import feor.Utils;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class InputFileFilter extends FileFilter
{
    @Override
    public boolean accept(final File f) {
        if (f.isDirectory()) {
            return true;
        }
        final String extension = Utils.getExtension(f);
        if (extension == null) {
            return false;
        }
        if (extension.equals(Utils.EXTENSION.csv.getExtension()) || extension.equals(Utils.EXTENSION.tsv.getExtension()) || extension.equals(Utils.EXTENSION.txt.getExtension())) {
            return true;
        }
        System.err.println(Utils.EXTENSION.getAcceptedExtensionsMessage());
        return false;
    }
    
    @Override
    public String getDescription() {
        return Utils.EXTENSION.getAcceptedExtensionsMessage();
    }
}
