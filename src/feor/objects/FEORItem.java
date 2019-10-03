//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor.objects;

import java.util.Objects;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;
import feor.html.HTMLReader;
import java.util.List;

public class FEORItem
{
    private FEORCode code;
    private FEORDescription title;
    private FEORDescription intro;
    private List<FEORDescription> tasks;
    private List<FEORDescription> roles;
    private List<FEORDescription> relatedRoles;
    private List<String> allTexts;
    private String shortString;
    
    public static FEORItem getInstance(final String feorHTMLFileName) throws IOException {
        return HTMLReader.readHTML(feorHTMLFileName);
    }
    
    public FEORItem(final String codeText, final String titleText, final String introText, final List<String> taskList, final List<String> roleList, final List<String> relatedRoleList) {
        this.intro = null;
        this.tasks = null;
        this.roles = null;
        this.relatedRoles = null;
        this.allTexts = null;
        this.shortString = null;
        this.code = new FEORCode(codeText);
        this.title = new FEORDescription(titleText);
        this.intro = new FEORDescription(introText);
        this.tasks = new ArrayList<FEORDescription>();
        for (final String item : taskList) {
            final FEORDescription desc = new FEORDescription(item);
            this.tasks.add(desc);
        }
        this.roles = new ArrayList<FEORDescription>();
        for (final String item : roleList) {
            final FEORDescription desc = new FEORDescription(item);
            this.roles.add(desc);
        }
        this.relatedRoles = new ArrayList<FEORDescription>();
        for (final String item : relatedRoleList) {
            final FEORDescription desc = new FEORDescription(item);
            this.relatedRoles.add(desc);
        }
    }
    
    public FEORCode getCode() {
        return this.code;
    }
    
    public FEORDescription getTitle() {
        return this.title;
    }
    
    public FEORDescription getIntro() {
        return this.intro;
    }
    
    public List<FEORDescription> getTasks() {
        return this.tasks;
    }
    
    public List<FEORDescription> getRoles() {
        return this.roles;
    }
    
    public List<FEORDescription> getRelatedRoles() {
        return this.relatedRoles;
    }
    
    public List<String> getAllTexts() {
        if (this.allTexts == null) {
            (this.allTexts = new ArrayList<String>()).add(this.getCode().getCodeString());
            this.allTexts.add(this.getTitle().getText());
            for (final FEORDescription desc : this.getTasks()) {
                this.allTexts.add(desc.getText());
            }
            for (final FEORDescription desc : this.getRoles()) {
                this.allTexts.add(desc.getText());
            }
            for (final FEORDescription desc : this.getRelatedRoles()) {
                this.allTexts.add(desc.getText());
            }
        }
        return this.allTexts;
    }
    
    public List<String> getTaskTexts() {
        final List<String> taskTexts = new ArrayList<String>();
        for (final FEORDescription desc : this.getTasks()) {
            taskTexts.add(desc.getText());
        }
        return taskTexts;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.code);
        hash = 37 * hash + Objects.hashCode(this.title);
        hash = 37 * hash + Objects.hashCode(this.intro);
        hash = 37 * hash + Objects.hashCode(this.tasks);
        hash = 37 * hash + Objects.hashCode(this.roles);
        return hash;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final FEORItem other = (FEORItem)obj;
        return Objects.equals(this.code, other.code) && Objects.equals(this.title, other.title) && Objects.equals(this.intro, other.intro) && Objects.equals(this.tasks, other.tasks) && Objects.equals(this.roles, other.roles);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FEORItem{");
        sb.append("\n  * code:  ").append(this.code.getCodeString());
        sb.append("\n  * title: ").append(this.title.getText());
        sb.append("\n  * intro: ").append(this.intro.getText());
        sb.append("\n  * roles:");
        for (final FEORDescription desc : this.roles) {
            sb.append("\n    - role: ").append(desc.getText());
        }
        sb.append("\n  * tasks:");
        for (final FEORDescription desc : this.tasks) {
            sb.append("\n    - task: ").append(desc.getText());
        }
        sb.append("\n  * relatedRoles:");
        for (final FEORDescription desc : this.relatedRoles) {
            sb.append("\n    - related role: ").append(desc.getText());
        }
        sb.append("\n}");
        return sb.toString();
    }
    
    public String toShortString() {
        if (this.shortString == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.code);
            sb.append(" = ");
            sb.append(this.title);
            this.shortString = sb.toString();
        }
        return this.shortString;
    }
}
