/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package html;

import java.net.URL;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author emm (Éva Mújdricza-Maydt, mujdricza@cl.uni-heidelberg.de)
 * @created 20170722
 * @note (The only original java file I found...)
 */
public class HTMLDownloader {

/*
	EXAMPLE in view-source:http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorlista.html
	
	<li><a class="folder main" href="#">3 EGYÉB FELSŐFOKÚ VAGY KÖZÉPFOKÚ KÉPZETTSÉGET IGÉNYLŐ FOGLALKOZÁSOK</a>
		<ul>
		<li><a class="folder" href="#">31 Technikusok és hasonló műszaki foglalkozások</a>
			<ul>
			<li><a class="folder" href="#">311 Ipari, építőipari technikusok</a>
				<ul>
				<li><a class="occ" href="3/3111.html">3111 Bányászati technikus</a></li>
				<li><a class="occ" href="3/3112.html">3112 Kohó- és anyagtechnikus</a></li>
				<li><a class="occ" href="3/3113.html">3113 Élelmiszer-ipari technikus</a></li>
				<li><a class="occ" href="3/3114.html">3114 Fa- és könnyűipari technikus</a></li>
				<li><a class="occ" href="3/3115.html">3115 Vegyésztechnikus</a></li>
				<li><a class="occ" href="3/3116.html">3116 Gépésztechnikus</a></li>
				<li><a class="occ last" href="3/3117.html">3117 Építő- és építésztechnikus</a></li>
				</ul>
			</li>
*/
	
	
	//String html = Jsoup.connect("http://stackoverflow.com").get().html();
	//Document document = Jsoup.connect("http://google.com").get();
	
	/** in "http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorlista.html" */
	private static final String RE_FEOR_ENTRY = ".+<a class=\"occ.+ href=\"([0-9]+/[0-9]+.html)\">.+";
	
	
	
	public static void main(String[] args) throws IOException {
		
		String feor_url = "http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorlista.html";
		//String main_url = "http://www.ksh.hu/";
		String main_url = "http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/";
		String output_path = "/Users/eva/my/uni/Exercises/JavaProj/src/html/feor/";
		String encoding = "iso-8859-2"; //"utf-8";
		download_feor_htmls(feor_url, main_url, encoding, output_path);
		
	}
	
	private static List<String> download_html(String url2download, String encoding) throws IOException {
		
        URL oracle = new URL(url2download);
		InputStream is = oracle.openStream();
        BufferedReader in = new BufferedReader(
			new InputStreamReader(is, Charset.forName(encoding)));
		List<String> lines = new ArrayList<String>();
        String inputLine = null;
        while ((inputLine = in.readLine()) != null) {
            //System.out.println(inputLine);
			lines.add(inputLine);
		}
		is.close();
		in.close();
		
		System.out.println(String.format("\t(read %s lines from %s)", new Object[]{lines.size(), url2download}));
		return lines;
	}
	
	private static void download_feor_htmls(String feor_url, String main_url, String encoding, String output_path) 
		throws IOException {
		String current_entry_url = null;
		try {
		List<String> feor_list_lines = download_html(feor_url, encoding);
		
		Pattern pat_feor_entry = Pattern.compile(RE_FEOR_ENTRY);
		
		for (String line : feor_list_lines) {
			Matcher match_feor_entry = pat_feor_entry.matcher(line);
			if (match_feor_entry.matches()) {
				String entry_url_fragment = match_feor_entry.group(1);
				
				String entry_url = main_url + entry_url_fragment;
				current_entry_url = entry_url;
				System.out.println("FOUND: " + entry_url);
				List<String> entry_lines = 
					//new ArrayList<String>();
					download_html(entry_url, encoding);
				write_entry(entry_url_fragment, entry_lines, encoding, output_path);
			} else {
				if (line.trim().startsWith("<li><a class=\"occ")) {
					System.err.println("!! NOT RECOGNIZED FEOR ENTRY: " + line);
				}
			}
		}
		} catch (Exception e) {
			//??
			System.out.println("!!! CATCHED Exception handling " + current_entry_url + ": " + e.getMessage());
		}
	}
	
	private static void write_entry(String url_fragment, List<String> lines, String encoding, String output_path) 
		throws IOException {
		
		String output_file_name = output_path + url_fragment.replace("/", "_");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_file_name), Charset.forName(encoding)));
		
		for (String line : lines) {
			bw.write(line);
			bw.newLine();
		}
		
		bw.close();
	}
	
	//not used now
	private static void download_with_htmlconnection(String url2download) throws MalformedURLException, IOException {
		
		try {
			URL myURL = new URL("http://example.com/");
			URLConnection myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			
			BufferedReader in = new BufferedReader(
				new InputStreamReader(myURLConnection.getInputStream(), Charset.forName("utf-8")));

			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
			in.close();
		} 
		catch (MalformedURLException e) { 
			// new URL() failed
			// ...
			String msg = e.getMessage();
			throw new MalformedURLException(msg);
		} 
		catch (IOException e) {   
			// openConnection() failed
			// ...
			String msg = e.getMessage();
			throw new IOException(msg);
		}
	}
	
}
