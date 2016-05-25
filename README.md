<img src='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/logo_5_banner.png' alt=''>

<h2>Overview/Announcements</h2>
rae-Lipid/RKMD (rae: Roger, Ankit, Elna) is a basic console and GUI-based screening tool which calculates the Reference Kendrick Mass Defect and the total number of carbon atoms (NCT) from a raw, experimental array of observed m/z values in order to identify m/z values which may represent compounds of a known lipid species. Input consists of the raw array of m/z ions as well as literature-derived RKMDs and NCTs through which to iterate and match with experimental data.<br>
<br>
<b>2014 Dec 16: A user manual has been released!</b> <a href='https://drive.google.com/file/d/0Bx4y1oB8xOUvMmh4c3NtTDFOdTg/view?usp=sharing'>(View)</a> or <a href='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/RKMD_UserGuide.pdf'>(Download)</a>. This version guides users through technical operation of raeLipID. The theory and background of the software is omitted but is planned to be incorporated into the user guide.<br>
<br>
<h2>Release Notes</h2>

<b>2015 Dec 13: Excel Tool for RKMD Calculation <a href='https://github.com/ankitrasto/rkmd/blob/master/dist/rae-lipID_HG_KMD_Calculator.xlsx'>(Direct Download)</a></b>
<ul> This is an excel tool developed by Roger and Elna to calculate KMD and cross check against nominal values as well. Click on "view raw" to use and download.   
    </ul>

<b>2015 Dec 13: RKMD v.0.6, commit 205ecbc <a href='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/raeLipid_v.0.6.zip'>(Direct Download)</a></b>
<ul><li>new back-end and front-end update with oxidized lipid screening support</li>
    <li>bug fixes and code restructuring for accurate ppm calculations</li>
    <li>please note that an updated user manual to reflect a few GUI changes is a work in progress</li>
    </ul>

<b>2015 Mar 21: RKMD <a href='https://code.google.com/p/rkmd/source/detail?r=29'>Revision 29</a>, v.0.5.4 (rae-LipID GUI) <a href='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/raeLipid_v.0.5.4.zip'>(Direct Download)</a></b>
<ul><li>formatting changes to output files for easier import into spreadsheet programs<br>
</li><li>changed default referenceKMD.csv file for fixes and to add more compounds</li></ul>

<b>2015 Feb 21: RKMD <a href='https://code.google.com/p/rkmd/source/detail?r=28'>Revision 28</a>, v.0.5.3 (rae-LipID GUI) <a href='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/raeLipid_v.0.5.3.zip'>(Direct Download)</a></b>
<ul><li>fixed bug in rae-LipID GUI that caused match results window to print ppm error instead of match name<br>
</li><li>added functionality for plasmalogen-class species: "Plasmalogen" can now be specified under the "FA_type" field of the referenceKMD.csv value. Additionally, any number of carbonyl chains "Ncc" can be specified as any integer value greater than 0 since the following equation is now used: http://tinyurl.com/eqnrkmd.

<br> Where N_ch represents the number of chains attached to the headgroup, N_TC represents the total number of carbon atoms in the fatty acid chains, and m_X are atomic masses for each element X.<br>
<ul><li>Further revisions are underway in which different degrees of oxidation for the fatty acid chains can be taken into account.</li></ul>

<b>2014 Dec 14: RKMD <a href='https://code.google.com/p/rkmd/source/detail?r=22'>Revision 22</a>, v.0.5.2 (rae-lipID GUI) <a href='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/RKMD_v.0.5.2.zip'>(Direct Download)</a></b>
<ul><li>output files now contain an additional field specifying the ppm error between a theoretically calculated mass and the observed mass; this gives a broad idea of the quality of the result being returned<br>
</li><li>retention time filtering has been added; two optional columns "TLOW" and "THIGH" can be specified in the referenceKMD.csv file for compounds, and a retention time filtering checkbox has been added to the GUI as an option for the user<br>
</li><li>to adjust the look and feel of the GUI for your platform, use the following launchers available in this download. For Windows users, double-click on <i>WindowsLaunch.exe</i> to run raeLipid; for Linux users, execute <i>LinuxLaunch.sh</i>. Manually adjust the look and feel (including font rendering fixes for Linux users) by running:</li></ul>

For Linux:<br>
<pre><code>java -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -Dswing.aatext=TRUE -Dawt.useSystemAAFontSettings=on -jar raeLipidGUI.jar
<br>
</code></pre>

For Windows:<br>
<pre><code>java -Dswing.defaultlaf=com.sun.java.swing.plaf.windows.WindowsLookAndFeel -jar raeLipidGUI.jar
<br>
</code></pre>


<b>2014 Oct 20: RKMD <a href='https://code.google.com/p/rkmd/source/detail?r=20'>Revision 20</a>, v.0.5.1 (rae-lipID GUI) <a href='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/RKMD_v.0.5.1.zip'>(Direct Download)</a></b>
<ul><li>Revised the RKMD back-end to include the functionality of specifying aliphatic or carbonyl fatty acid chains in the referenceKMD.csv file, as their carbon number calculations slightly differ. A new column in the referenceKMD.csv file named "FA_type" is mandatory and must specify either "aliphatic" or "carbonyl" as the FA_type for all species.<br>
</li><li>no GUI changes in this revision</li></ul>

<b>2014 Oct 18: RKMD <a href='https://code.google.com/p/rkmd/source/detail?r=18'>Revision 18</a>, v.0.5 (rae-lipID GUI) <a href='https://drive.google.com/uc?id=0Bx4y1oB8xOUvMXZpWkktbkJlaTA&export=download'>(Direct Download)</a></b>
<ul><li>This revision updates the GUI to the new rae-lipID front end. The GUI has been overhauled to be more user-friendly and to optimize workflow for repetitive calculations. The user-interface should be self-explanatory for the most part but a user manual will be published for more nuanced details (specific formatting requirements, etc.)<br>
</li><li>A sample .csv file for input has been provided in this download.<br>
</li><li>Special thanks to <a href='http://hawksmont.com/'>Hawskmont</a> for the artwork brushes that were used in the rae-lipID's logo's design</li></ul>

<b>See the Release_History wiki page for previous release notes</b>

<h1>Implementation Notes</h1>
Full implementation notes are available in the project's google drive folder (downloads for google-code hosted projects have now been deprecated). The following UML diagram is a rough outline as implemented since <a href='https://code.google.com/p/rkmd/source/detail?r=2'>revision 2</a>:<br>
<br>
<img src='https://googledrive.com/host/0Bx4y1oB8xOUva21FdVY4OGtUTjg/UML_1.png' alt='' width='654' height='600'>


<b><a href='https://drive.google.com/folderview?id=0Bx4y1oB8xOUva21FdVY4OGtUTjg&usp=sharing'>Google Drive End-User Downloads</a></b>
