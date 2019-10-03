// 
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.io.FileOutputStream;
import feor.objects.FEORDescription;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.IOException;
import java.util.Map;
import feor.objects.FEORItem;
import java.util.List;
import feor.objects.FEORSearch;
import java.io.File;

public class Main
{
    public static void main(final String[] args) throws IOException {
        final String currentPath = new File(".").getAbsolutePath();
        final String pathToFEORHTMLFiles = new File(currentPath + "/feorhtmls/").getCanonicalPath();
        final FEORSearch.SEARCHMODE searchMode = FEORSearch.SEARCHMODE.simpleStringOnAllDescriptions;
        final List<FEORItem> FEORList = readFEORList(pathToFEORHTMLFiles);
        System.out.println("READ LIST: " + FEORList.size());
        if (args.length > 0) {
            final String embedFileName = args[0];
            final Map<String, List<String>> testInstancesRoles = extractRoleTests(FEORList, embedFileName);
            testRoles(FEORList, testInstancesRoles, searchMode);
        }
    }
    
    private static void scanInputs(final List<FEORItem> FEORList, final FEORSearch.SEARCHMODE searchMode) {
        final Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("q")) {
            System.out.println("Job description: ");
            input = scanner.nextLine();
            final List<FEORItem> foundItems = FEORSearch.search(FEORList, input, searchMode);
            System.out.println("--> found " + foundItems.size() + " items");
            for (final FEORItem item : foundItems) {
                System.out.println("  - " + item.toShortString());
            }
        }
    }
    
    static List<FEORItem> readFEORList(final String pathToFEORHTMLs) throws IOException {
        final List<FEORItem> feorList = new ArrayList<FEORItem>();
        final String[] list;
        final String[] fileNames = list = new File(pathToFEORHTMLs).list();
        for (final String fileName : list) {
            final FEORItem item = FEORItem.getInstance(pathToFEORHTMLs + "/" + fileName);
            feorList.add(item);
        }
        System.out.println("=> read FEOR LIST with " + feorList.size() + " ITEMS.");
        return feorList;
    }
    
    static List<FEORItem> readFEORList() throws IOException {
        final String currentPath = new File(".").getAbsolutePath();
        final String pathToFEORHTMLFiles = new File(currentPath + "/feorhtmls/").getCanonicalPath();
        System.out.println("pathToFEORHTMLFiles: " + pathToFEORHTMLFiles);
        return readFEORList(pathToFEORHTMLFiles);
    }
    
    private static Map<String, List<String>> extractRoleTests(final List<FEORItem> FEORList, final String embedFileName) throws IOException {
        final Map<String, List<String>> embedTestMap = new HashMap<String, List<String>>();
        for (final FEORItem item : FEORList) {
            final String cs = item.getCode().getCodeString();
            final List<FEORDescription> roles = item.getRoles();
            for (final FEORDescription role : roles) {
                final String rt = role.getText();
                if (!embedTestMap.containsKey(rt)) {
                    embedTestMap.put(rt, new ArrayList<String>());
                }
                else {
                    System.err.println("ROLE ALREADY IN TEST MAP: " + rt + " = " + embedTestMap.get(rt) + " (new: " + cs + ")");
                }
                embedTestMap.get(rt).add(cs);
            }
        }
        if (embedFileName != null) {
            writeCSV(embedTestMap, embedFileName, "\t");
        }
        return embedTestMap;
    }
    
    private static void writeCSV(final Map<String, List<String>> embedTestMap, final String csvFileName, final String separator) throws IOException {
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(csvFileName)), Charset.forName("iso-8859-2")));
        for (final String text : embedTestMap.keySet()) {
            final List<String> code = embedTestMap.get(text);
            bw.write(text);
            bw.write(separator);
            bw.write(String.join(separator, code));
            bw.newLine();
        }
        bw.close();
    }
    
    public static String searchString(final List<FEORItem> feorItemList, final String inputText, final FEORSearch.SEARCHMODE searchMode) {
        final List<FEORItem> results = FEORSearch.search(feorItemList, inputText, searchMode);
        final StringBuilder sb = new StringBuilder();
        for (final FEORItem item : results) {
            final String code = item.getCode().getCodeString();
            final String title = item.getTitle().getText();
            sb.append(code).append(Utils.FORMAT.TABULATOR.getValue()).append(title).append(Utils.FORMAT.TABULATOR.getValue()).append(Utils.FORMAT.NEWLINE.getValue());
        }
        return sb.toString();
    }
    
    public static String searchFile(final List<FEORItem> feorItemList, final String inputFileName, final String charsetName, final FEORSearch.SEARCHMODE searchMode) {
        List<String> inputTextLines = null;
        try {
            inputTextLines = Utils.readInputFile(inputFileName, charsetName);
        }
        catch (IOException ioe) {
            final String msg = "CANNOT OPEN FILE " + inputFileName;
            return msg;
        }
        final List<List<FEORItem>> results = FEORSearch.searchDescriptionList(feorItemList, inputTextLines, searchMode);
        final StringBuilder sb = new StringBuilder();
        for (int index = 0; index < inputTextLines.size(); ++index) {
            final List<FEORItem> items = results.get(index);
            sb.append(items.size()).append(" results for: '");
            sb.append(inputTextLines.get(index)).append("'").append(Utils.FORMAT.NEWLINE.getValue());
            for (final FEORItem item : items) {
                final String code = item.getCode().getCodeString();
                final String title = item.getTitle().getText();
                sb.append(Utils.FORMAT.TABULATOR.getValue()).append(code).append(Utils.FORMAT.TABULATOR.getValue()).append(title).append(Utils.FORMAT.NEWLINE.getValue());
            }
            sb.append(Utils.FORMAT.NEWLINE.getValue());
        }
        return sb.toString();
    }
    
    private static void testRoles(final List<FEORItem> FEORList, final Map<String, List<String>> testInstances, final FEORSearch.SEARCHMODE searchMode) {
        System.out.println("TEST " + testInstances.size() + " instances.");
        final int all = testInstances.size();
        int correct = 0;
        int i = 0;
        for (final String text : testInstances.keySet()) {
            ++i;
            final List<String> goldCodes = testInstances.get(text);
            System.out.println(i + "/" + all + " TEST\t" + goldCodes + "\t=\t" + text);
            final List<FEORItem> foundItems = FEORSearch.search(FEORList, text, searchMode);
            final List<String> foundCodes = new ArrayList<String>();
            for (final FEORItem item : foundItems) {
                foundCodes.add(item.getCode().getCodeString());
            }
            boolean isCorrect = true;
            for (final String gold : goldCodes) {
                if (!foundCodes.contains(gold)) {
                    isCorrect = false;
                }
            }
            String prefix = "false";
            if (isCorrect) {
                ++correct;
                prefix = "true";
            }
            System.out.println(" => " + prefix + "\t" + foundCodes);
        }
        System.out.println("CORRECT: " + correct + " = " + correct * 1.0 / all * 100.0 + "%");
    }
    
    public static String lookupCode(final Map<String, FEORItem> feorCode2ItemMap, final String code2Lookup) {
        final StringBuilder sb = new StringBuilder();
        if (feorCode2ItemMap.containsKey(code2Lookup)) {
            sb.append(feorCode2ItemMap.get(code2Lookup).toString()).append(Utils.FORMAT.NEWLINE.getValue());
        }
        else {
            sb.append("No FEOR08 item with code '" + code2Lookup + "' found.").append(Utils.FORMAT.NEWLINE.getValue());
        }
        return sb.toString();
    }
}
