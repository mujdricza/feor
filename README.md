# feor

Simple, rule-based searching tool for job codes in FEOR-08 (Foglalkozások Egységes Osztályozási Rendszere), the hungarian version of the Standard Occupational Classification (SOC)
provided by the Központi Statistikai Hivatal (KSH, Hungarian Central Statistical Office).

* The job definitions used within the tool can be found here:
[http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorlista.html](http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorlista.html)

* The used content is available under the following licence: [http://www.ksh.hu/docs/osztalyozasok/osztalyozasok_felhasznalasi_feltetelek.pdf](http://www.ksh.hu/docs/osztalyozasok/osztalyozasok_felhasznalasi_feltetelek.pdf) (downloadable from [http://www.ksh.hu/feor_menu](http://www.ksh.hu/feor_menu))

## Description

The GUI allows for lookup the FEOR-08 job codes according to a job description. The description can be also just a word or a longer text. 

For now, only a string-based search is implemented. The tool searhes for words in the given description within all job definition pages. 
The web pages should be downloaded in a subdirectory like `feorhtmls` (provided with the tool). 

The output contains all *possible* job codes, t.i. all codes with job definition fitting to the input job description.

## Use

### Prerequisites

Java 8 or newer

### Jar

There are two jar files compiled in 2017 (GUI):
* v1_jar/FEOR08Search_latin2input.jar (for latin-2 input files)
* v1_jar/FEOR08Search_utf8input.jar (for utf-8 input files)

Just double click on one of them. If you have launched the java files with JavaLauncher, they will open the GUI.

Alternatively, the GUI can be started from the ocmmand line:
```
java -jar FEOR08Search_utf8input.jar
```

Select a text-based file with one job description per line. 
Click on 'Select'.
The output with the possible FEOR-08 codes will be listed for each job description on the right box 'Output Fields'.

If you would like to check the original description of a job with a specific code, write the code number in the 'Code lookup' text field. On the bottom right field, all information will be shown in a structured format. 

### Source

Note that the source code in folder `src` are **not the original ones**, but they are automatically decompiled by Procyon (v0.5.36) on [http://www.javadecompilers.com/](http://www.javadecompilers.com/).
(The reason is that - unfortunately - the original source code has got lost.)

Thus, all JavaDocs and all comments are missing in the java files, and it cannot be assured that the functionality in the jar files is the same as the java files offer in the `src`folder. 

# KSH tool for FEOR-08 search



The KSH also provies a web search interface:
[http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorsearch.html](http://www.ksh.hu/docs/szolgaltatasok/hun/feor08/feorsearch.html)

## Further information

I found the report [Automating Survey Coding for Occupation](http://doku.iab.de/fdz/reporte/2014/MR_10-14_EN.pdf) by Malte Schierholz very inspiring. 

# Contact

Éva Mújdricza-Maydt (me.levelek@gmx.de)