//  
// Originally written by Eva Mujdricza-Maydt, unfortunately, the source files are lost
// Decompiled by Procyon v0.5.36
// 

package feor.html;

import java.net.URLConnection;
import java.net.MalformedURLException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.net.URL;
import java.util.List;
import java.io.IOException;

public class HTMLDownloader
{
    private static final String RE_FEOR_ENTRY = ".+<a class=\"occ.+ href=\"([0-9]+/[0-9]+.html)\">.+";
    
    public static void main(final String[] args) throws IOException {
        final String feor_url = "http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorlista.html";
        final String main_url = "http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/";
        final String output_path = "html/feorhtmls/";
        final String encoding = "iso-8859-2";
        download_feor_htmls(feor_url, main_url, encoding, output_path);
    }
    
    private static List<String> download_html(final String url2download, final String encoding) throws IOException {
        final URL oracle = new URL(url2download);
        final InputStream is = oracle.openStream();
        final BufferedReader in = new BufferedReader(new InputStreamReader(is, Charset.forName(encoding)));
        final List<String> lines = new ArrayList<String>();
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            lines.add(inputLine);
        }
        is.close();
        in.close();
        System.out.println(String.format("\t(read %s lines from %s)", lines.size(), url2download));
        return lines;
    }
    
    private static void download_feor_htmls(final String feor_url, final String main_url, final String encoding, final String output_path) throws IOException {
        String current_entry_url = null;
        try {
            final List<String> feor_list_lines = download_html(feor_url, encoding);
            final Pattern pat_feor_entry = Pattern.compile(".+<a class=\"occ.+ href=\"([0-9]+/[0-9]+.html)\">.+");
            for (final String line : feor_list_lines) {
                final Matcher match_feor_entry = pat_feor_entry.matcher(line);
                if (match_feor_entry.matches()) {
                    final String entry_url_fragment = match_feor_entry.group(1);
                    final String entry_url = current_entry_url = main_url + entry_url_fragment;
                    System.out.println("FOUND: " + entry_url);
                    final List<String> entry_lines = download_html(entry_url, encoding);
                    write_entry(entry_url_fragment, entry_lines, encoding, output_path);
                }
                else {
                    if (!line.trim().startsWith("<li><a class=\"occ")) {
                        continue;
                    }
                    System.err.println("!! NOT RECOGNIZED FEOR ENTRY: " + line);
                }
            }
        }
        catch (Exception e) {
            System.out.println("!!! CATCHED Exception handling " + current_entry_url + ": " + e.getMessage());
        }
    }
    
    private static void write_entry(final String url_fragment, final List<String> lines, final String encoding, final String output_path) throws IOException {
        final String output_file_name = output_path + url_fragment.replace("/", "_");
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_file_name), Charset.forName(encoding)));
        for (final String line : lines) {
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }
    
    private static void download_with_htmlconnection(final String url2download) throws MalformedURLException, IOException {
        try {
            final URL myURL = new URL("http://example.com/");
            final URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            final BufferedReader in = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream(), Charset.forName("utf-8")));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
        }
        catch (MalformedURLException e) {
            final String msg = e.getMessage();
            throw new MalformedURLException(msg);
        }
        catch (IOException e2) {
            final String msg = e2.getMessage();
            throw new IOException(msg);
        }
    }
}
