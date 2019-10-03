//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor.html;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import feor.Utils;
import feor.objects.FEORItem;
import java.util.regex.Pattern;

public class HTMLReader
{
    private static String fileName;
    private static Pattern codeTitleLinePattern;
    private static Pattern introLinePattern;
    private static Pattern taskLinePattern;
    private static Pattern roleLinePattern;
    private static Pattern relatedLinePattern;
    
    public static FEORItem readHTML(final String feorHTMLFileName) throws FileNotFoundException, IOException {
        HTMLReader.fileName = feorHTMLFileName;
        final List<String> lines = Utils.getLines(feorHTMLFileName, null);
        final String code = getCode(lines);
        final String title = getTitle(lines);
        final String intro = getIntro(lines);
        final List<String> tasks = getTasks(lines);
        final List<String> roles = getRoles(lines);
        final List<String> relateds = getRelatedCodes(lines);
        final FEORItem item = new FEORItem(code, title, intro, tasks, roles, relateds);
        return item;
    }
    
    private static String getCode(final List<String> lines) {
        String code = null;
        for (final String line : lines) {
            if (line.startsWith("<h2")) {
                final Matcher mat = HTMLReader.codeTitleLinePattern.matcher(line);
                if (mat.matches()) {
                    code = mat.group(RE.code.name());
                    break;
                }
                System.err.println(String.format("!!! IN FILE %s: NOT FOUND CODE IN h2 LINE: %s", HTMLReader.fileName, line));
            }
        }
        return code;
    }
    
    private static String getTitle(final List<String> lines) {
        String title = null;
        for (final String line : lines) {
            if (line.startsWith("<h2")) {
                final Matcher mat = HTMLReader.codeTitleLinePattern.matcher(line);
                if (mat.matches()) {
                    title = mat.group(RE.title.name());
                    break;
                }
                System.err.println(String.format("!!! IN FILE %s: NOT FOUND TITLE IN h2 LINE: %s", HTMLReader.fileName, line));
            }
        }
        return title;
    }
    
    private static String getIntro(final List<String> lines) {
        String intro = null;
        for (final String line : lines) {
            if (line.startsWith("<p ") && line.contains("occ_intro")) {
                final Matcher mat = HTMLReader.introLinePattern.matcher(line);
                if (mat.matches()) {
                    intro = mat.group(RE.intro.name());
                    break;
                }
                System.err.println(String.format("!!! IN FILE %s: NOT FOUND INTRO IN p LINE: %s", HTMLReader.fileName, line));
            }
        }
        return intro;
    }
    
    private static List<String> getTasks(final List<String> lines) {
        final List<String> tasks = new ArrayList<String>();
        boolean isActive = false;
        for (final String line : lines) {
            if (isActive) {
                if (line.startsWith("</ol>")) {
                    break;
                }
                final Matcher mat = HTMLReader.taskLinePattern.matcher(line);
                if (mat.matches()) {
                    final String task = mat.group(RE.task.name());
                    tasks.add(task);
                }
                else {
                    System.err.println(String.format("!!! IN FILE %s: NOT FOUND TASK IN li LINE: %s", HTMLReader.fileName, line));
                }
            }
            else {
                if (!line.startsWith("<ol ") || !line.contains("occ_task")) {
                    continue;
                }
                isActive = true;
            }
        }
        return tasks;
    }
    
    private static List<String> getRoles(final List<String> lines) {
        final List<String> roles = new ArrayList<String>();
        boolean isActive = false;
        for (final String line : lines) {
            if (isActive) {
                if (line.startsWith("</ul>")) {
                    break;
                }
                final Matcher mat = HTMLReader.roleLinePattern.matcher(line);
                if (mat.matches()) {
                    final String role = mat.group(RE.role.name());
                    roles.add(role);
                }
                else {
                    System.err.println(String.format("!!! IN FILE %s: NOT FOUND ROLE IN li LINE: %s", HTMLReader.fileName, line));
                }
            }
            else {
                if (!line.startsWith("<ul ") || !line.contains("occ_role")) {
                    continue;
                }
                isActive = true;
            }
        }
        return roles;
    }
    
    private static List<String> getRelatedCodes(final List<String> lines) {
        final List<String> relatedCodes = new ArrayList<String>();
        boolean isActive = false;
        for (final String line : lines) {
            if (isActive) {
                if (line.startsWith("</ul>")) {
                    break;
                }
                final Matcher mat = HTMLReader.relatedLinePattern.matcher(line);
                if (mat.matches()) {
                    final String code = mat.group(RE.code.name());
                    relatedCodes.add(code);
                }
                else {
                    System.err.println(String.format("!!! IN FILE %s: NOT FOUND RELATED CODE IN li LINE: %s", HTMLReader.fileName, line));
                }
            }
            else {
                if (!line.startsWith("<ul ") || !line.contains("occ_related")) {
                    continue;
                }
                isActive = true;
            }
        }
        return relatedCodes;
    }
    
    static {
        HTMLReader.fileName = null;
        HTMLReader.codeTitleLinePattern = Pattern.compile("<h2 class=\"occ_title\">(?<code>[0-9]{4}) (?<title>[^<]+)</h2>");
        HTMLReader.introLinePattern = Pattern.compile("<p class=\"occ_intro\">(?<intro>[^<]+)</p>");
        HTMLReader.taskLinePattern = Pattern.compile("<li>(?<task>[^<]+)</li>");
        HTMLReader.roleLinePattern = Pattern.compile("<li>(?<role>[^<]+)</li>");
        HTMLReader.relatedLinePattern = Pattern.compile("<li><a href=\"[^\"]+\">(?<code>[0-9]{4}) ([^<]+)</a></li>");
    }
    
    private enum HTML
    {
        h2, 
        p, 
        ol, 
        li, 
        occ_title, 
        occ_intro, 
        occ_label, 
        occ_task, 
        occ_related;
    }
    
    private enum RE
    {
        code, 
        title, 
        intro, 
        task, 
        role;
    }
}
