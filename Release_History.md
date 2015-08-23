# This page contains release notes for revisions that were previously featured on the project's main page and available for download.

# Release History #


**2014 Sep 19: RKMD [Revision 14](https://code.google.com/p/rkmd/source/detail?r=14), v.0.4  [(Download)](https://drive.google.com/folderview?id=0Bx4y1oB8xOUva21FdVY4OGtUTjg&usp=sharing)**
  * This revision includes fixes to the GUI: Direct text-input to the text area of the RKMD front-end should work. The first line is always a text header and multiple columns are specified in the CSV-style format (see the sample masses3.csv file attached). There must be no trailing white spaces or extra empty lines in both the file and GUI m/z inputs.


**2014 Jul 26: RKMD [Revision 13](https://code.google.com/p/rkmd/source/detail?r=13), v.0.3  [(Download)](https://drive.google.com/folderview?id=0Bx4y1oB8xOUva21FdVY4OGtUTjg&usp=sharing)**
  * This revision includes fixes to the rkmdBackEnd: An additional column specifying the maximum number of double-bonds has been added to the referenceKMD.csv file. Specifying this column is required for the program to work (revisions 13 and later).
  * The maximum permissible calculable rkmd is now less than or equal to 0.5. (as opposed to rkmd <= 0 in previous versions).
  * **The GUI-input form for experimental m/z data is dysfunctional.** Until this is re-implemented, you must load the experimental data directly from a CSV file; the format has been changed to allow additional columns for ease of reference after calculations. A sample CSV file with masses and retention times are included in this download

**2014 Jul 20: RKMD [Revision 12](https://code.google.com/p/rkmd/source/detail?r=12), v.0.2 [(Download)](https://drive.google.com/folderview?id=0Bx4y1oB8xOUva21FdVY4OGtUTjg&usp=sharing)**
  * This revision includes bug and formatting fixes. A negative/positive filter has been added to the GUI.
  * The GUI-input form for experimental m/z data is dysfunctional. Until this is re-implemented, you must load the experimental data directly from a CSV file; the format has been changed to allow additional columns for ease of reference after calculations. A sample CSV file with masses and retention times are included in this download
  * The input file (CSV) must be comma-delimited and contain at least one column. It is best edited in a spreadsheet application.

**2014 May 18: RKMD [Revision 11](https://code.google.com/p/rkmd/source/detail?r=11), v.0.1 [(Download)](https://drive.google.com/folderview?id=0Bx4y1oB8xOUva21FdVY4OGtUTjg&usp=sharing)**
  * The available .zip file contains the GUI (packaged as an executable rkmdGUI.jar and contains the most recently revised back-end) and two user-modifiable .csv files which contain elemental masses and literature KMD values. Since rkmdGUI.jar depends on the two .csv files, the directory structure must not be modified and left intact. To run RKMD v.0.1,
  1. Ensure that a relatively recent Java Runtime Environment (JRE 7 or later) is installed.
  1. Download and extract the relevant .zip file on the Google-Drive site.
  1. Open a command-prompt or terminal in the directory containing rkmdGUI.jar
  1. Execute the command:
```
java -jar rkmdGUI.jar 
```
  1. It is important to monitor the terminal output for any messages produced from RKMD.