//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor.objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Iterator;
import feor.Utils;
import java.util.List;

public class FEORSearch
{
    private static String splitters;
    private static List<String> stopwords;
    
    public static List<FEORItem> search(final List<FEORItem> feorItemList, final String inputText, final SEARCHMODE mode) {
        List<FEORItem> results = null;
        if (mode == null) {
            results = simpleStringSearchOnAllDescriptions(feorItemList, inputText);
        }
        else if (mode.equals(SEARCHMODE.simpleStringOnAllDescriptions)) {
            results = simpleStringSearchOnAllDescriptions(feorItemList, inputText);
        }
        else {
            if (!mode.equals(SEARCHMODE.simpleStringOnTaskDescriptions)) {
                final String msg = "NOT IMPLEMENTED search mode: " + mode.name();
                throw new RuntimeException(msg);
            }
            results = simpleStringSearchOnTaskDescriptions(feorItemList, inputText);
        }
        return results;
    }
    
    public static List<List<FEORItem>> searchFile(final List<FEORItem> feorItemList, final String inputFileName, final String charsetName, final SEARCHMODE mode) throws IOException {
        final List<String> inputTextLines = Utils.readInputFile(inputFileName, charsetName);
        final List<List<FEORItem>> results = null;
        for (final String inputText : inputTextLines) {
            final List<FEORItem> resultsForLine = search(feorItemList, inputText, mode);
            results.add(resultsForLine);
        }
        return results;
    }
    
    public static List<List<FEORItem>> searchDescriptionList(final List<FEORItem> feorItemList, final List<String> inputTextLines, final SEARCHMODE mode) {
        final List<List<FEORItem>> results = new ArrayList<List<FEORItem>>();
        for (final String inputText : inputTextLines) {
            final List<FEORItem> resultsForLine = search(feorItemList, inputText, mode);
            results.add(resultsForLine);
        }
        return results;
    }
    
    private static List<FEORItem> simpleStringSearchOnAllDescriptions(final List<FEORItem> feorItemList, final String inputText) {
        final List<String> inputTextUnits = getUnits(inputText);
        final List<FEORItem> results = new ArrayList<FEORItem>();
        for (final FEORItem item : feorItemList) {
            final List<String> common = new ArrayList<String>();
            final List<String> itemDescriptions = item.getAllTexts();
            for (final String desc : itemDescriptions) {
                final List<String> descUnits = getUnits(desc);
                common.addAll(intersection(inputTextUnits, descUnits));
            }
            if (!common.isEmpty()) {
                results.add(item);
            }
        }
        return results;
    }
    
    private static List<FEORItem> simpleStringSearchOnTaskDescriptions(final List<FEORItem> feorItemList, final String inputText) {
        final List<String> inputTextUnits = getUnits(inputText);
        final List<FEORItem> results = new ArrayList<FEORItem>();
        for (final FEORItem item : feorItemList) {
            final List<String> common = new ArrayList<String>();
            final List<String> itemDescriptions = item.getTaskTexts();
            for (final String desc : itemDescriptions) {
                final List<String> descUnits = getUnits(desc);
                common.addAll(intersection(inputTextUnits, descUnits));
            }
            if (!common.isEmpty()) {
                results.add(item);
            }
        }
        return results;
    }
    
    private static List<String> getUnits(String inputText) {
        inputText = inputText.toLowerCase();
        final String[] unitArray = inputText.split(FEORSearch.splitters);
        List<String> units = Arrays.asList(unitArray);
        units = difference(units, getStopWords());
        return units;
    }
    
    private static List<String> getStopWords() {
        if (FEORSearch.stopwords == null) {
            (FEORSearch.stopwords = new ArrayList<String>()).add("a");
            FEORSearch.stopwords.add("az");
            FEORSearch.stopwords.add("egy");
            FEORSearch.stopwords.add("\u00e9s");
            FEORSearch.stopwords.add("vagy");
            FEORSearch.stopwords.add("hogy");
            FEORSearch.stopwords.add("pl");
            FEORSearch.stopwords.add("munka");
            FEORSearch.stopwords.add("\u00e1ll\u00e1s");
            FEORSearch.stopwords.add("\u00e1ll\u00e1saj\u00e1nlat");
        }
        return FEORSearch.stopwords;
    }
    
    public static <T> List<T> intersection(final List<T> list1, final List<T> list2) {
        final List<T> list3 = new ArrayList<T>();
        for (final T t : list1) {
            if (list2.contains(t)) {
                list3.add(t);
            }
        }
        return list3;
    }
    
    public static <T> List<T> difference(final List<T> list1, final List<T> list2) {
        final List<T> list3 = new ArrayList<T>();
        for (final T t : list1) {
            if (!list2.contains(t)) {
                list3.add(t);
            }
        }
        return list3;
    }
    
    static {
        FEORSearch.splitters = "[\\s,;.:?()]+";
        FEORSearch.stopwords = null;
    }
    
    public enum SEARCHMODE
    {
        simpleStringOnAllDescriptions, 
        simpleStringOnTaskDescriptions;
    }
}
