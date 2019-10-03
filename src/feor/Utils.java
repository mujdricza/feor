//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.List;
import java.io.File;

public class Utils
{
    public static String getExtension(final File f) {
        String ext = null;
        final String s = f.getName();
        final int i = s.lastIndexOf(FORMAT.FINAL_STOP.getValue());
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i).toLowerCase();
        }
        return ext;
    }
    
    public static List<String> readInputFile(final String inputFileName, final String charsetName) throws IOException {
        return getLines(inputFileName, charsetName);
    }
    
    public static List<String> getLines(final String inputFileName, final String charsetName) throws FileNotFoundException, IOException {
        final Charset chset = (charsetName == null) ? Charset.forName(FORMAT.DEFAULTCHARSET.getValue()) : Charset.forName(charsetName);
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFileName)), chset));
        final List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }
        br.close();
        return lines;
    }
    
    public enum FORMAT
    {
        FINAL_STOP("."), 
        TABULATOR("\t"), 
        NEWLINE("\n"), 
        DEFAULTCHARSET("iso-8859-2"), 
        CHARSETUTF8("utf-8");
        
        private final String value;
        
        private FORMAT(final String valueString) {
            this.value = valueString;
        }
        
        public String getValue() {
            return this.value;
        }
    }
    
    public enum EXTENSION
    {
        csv(".csv", "csv: comma separated tabular format: one item per line, components of an item are separated by comma (,)"), 
        tsv(".tsv", "tsv: tabulator separated tabular format: one item per line, components of an item are separated by a tabulator (\t)"), 
        txt(".txt", "txt: simple text format: one item per line");
        
        private final String extension;
        private final String description;
        private static List<String> possibleExtensions;
        
        private EXTENSION(final String extensionString, final String descriptionForFileWithExtension) {
            this.extension = extensionString;
            this.description = descriptionForFileWithExtension;
        }
        
        public String getExtension() {
            return this.extension;
        }
        
        public String getExtensionDescription() {
            return this.description;
        }
        
        public static List<String> getPossibleExtensions() {
            if (EXTENSION.possibleExtensions == null) {
                EXTENSION.possibleExtensions = new ArrayList<String>();
                for (final EXTENSION e : values()) {
                    EXTENSION.possibleExtensions.add(e.getExtension());
                }
            }
            return EXTENSION.possibleExtensions;
        }
        
        public static String getAcceptedExtensionsMessage() {
            final String msg = "Accept files with extensions: " + String.join(", ", getPossibleExtensions());
            return msg;
        }
        
        static {
            EXTENSION.possibleExtensions = null;
        }
    }
}
