package edu.uiuc.cs.fsl.propertydocs.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;

//change stats 
//disable colors

public class FinishUp {
      private static final String HTMLHEADER = 
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
    + "<!--NewPage-->\n"
    + "<HTML>\n"
    + "<HEAD>\n"
    + "<!-- Generated by docsp on " + Util.getDate() + " -->\n"
    + "<TITLE>\n"
    + "Test\n"
    + "</TITLE>\n"
    + "\n"
    + "<META NAME=\"date\" CONTENT=\"2011-07-13\">\n"
    + "\n"
    + "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"stylesheet.css\" TITLE=\"Style\">\n"
    + "\n"
    + "<SCRIPT type=\"text/javascript\">\n"
    + "function windowTitle()\n"
    + "{\n"
    + "    if (location.href.indexOf('is-external=true') == -1) {\n"
    + "        parent.document.title=\"Test\";\n"
    + "    }\n"
    + "}\n"
    + "</SCRIPT>\n"
    + "<NOSCRIPT>\n"
    + "</NOSCRIPT>\n"
    + "\n"
    + "</HEAD>\n"
    + "\n"
    + "<BODY BGCOLOR=\"white\" onload=\"windowTitle();\">\n"
    + "<HR>\n"
    + "\n"
    + "\n"
    + "<!-- ========= START OF TOP NAVBAR ======= -->\n"
    + "<A NAME=\"navbar_top\"><!-- --></A>\n"
    + "<A HREF=\"#skip-navbar_top\" title=\"Skip navigation links\"></A>\n"
    + "<TABLE BORDER=\"0\" WIDTH=\"100%\" CELLPADDING=\"1\" CELLSPACING=\"0\" SUMMARY=\"\">\n"
    + "<TR>\n"
    + "<TD COLSPAN=2 BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">\n"
    + "<A NAME=\"navbar_top_firstrow\"><!-- --></A>\n"
    + "<TABLE BORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"3\" SUMMARY=\"\">\n"
    + "  <TR ALIGN=\"center\" VALIGN=\"top\">\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"package-summary.html\"><FONT CLASS=\"NavBarFont1\"><B>Package</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1Rev\"> &nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>Class</B></FONT>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"package-tree.html\"><FONT CLASS=\"NavBarFont1\"><B>Tree</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"deprecated-list.html\"><FONT CLASS=\"NavBarFont1\"><B>Deprecated</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"index-all.html\"><FONT CLASS=\"NavBarFont1\"><B>Index</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"help-doc.html\"><FONT CLASS=\"NavBarFont1\"><B>Help</B></FONT></A>&nbsp;</TD>\n"
    + "  </TR>\n"
    + "</TABLE>\n"
    + "</TD>\n"
    + "<TD ALIGN=\"right\" VALIGN=\"top\" ROWSPAN=3><EM>\n"
    + "<TD BGCOLOR=#FFFFFF CLASS=\"NavBarCell1Rev\">  <A HREF='property-list.html'><FONT CLASS=NavBarFont1Rev><B>Properties</B></FONT></EM>\n"
    + "</TR>\n"
    + "\n"
//    + "<TD BGCOLOR=\"white\" CLASS=\"NavBarCell2\"><FONT SIZE=\"-2\">\n"
//    + "  <A HREF=\"index.html?Test.html\" target=\"_top\"><B>FRAMES</B></A>  &nbsp;\n"
//    + "&nbsp;<A HREF=\"Test.html\" target=\"_top\"><B>NO FRAMES</B></A>  &nbsp;\n"
    + "&nbsp;<SCRIPT type=\"text/javascript\">\n"
    + "  <!--\n"
    + "  if(window==top) {\n"
    + "    document.writeln('<A HREF=\"allclasses-noframe.html\"><B>All Classes</B></A>');\n"
    + "  }\n"
    + "  //-->\n"
    + "</SCRIPT>\n"
    + "<NOSCRIPT>\n"
    + "  <A HREF=\"allclasses-noframe.html\"><B>All Classes</B></A>\n"
    + "</NOSCRIPT>\n"
    + "\n"
    + "\n"
    + "</FONT></TD>\n"
    + "</TR>\n"
    + "</TABLE>\n"
    + "<A NAME=\"skip-navbar_top\"></A>\n"
    + "<!-- ========= END OF TOP NAVBAR ========= -->\n"
    + "<HR>\n";

    private static final String HTMLFOOTER = 
      "<!-- ======= START OF BOTTOM NAVBAR ====== -->\n"
    + "<A NAME=\"navbar_bottom\"><!-- --></A>\n"
    + "<A HREF=\"#skip-navbar_bottom\" title=\"Skip navigation links\"></A>\n"
    + "<TABLE BORDER=\"0\" WIDTH=\"100%\" CELLPADDING=\"1\" CELLSPACING=\"0\" SUMMARY=\"\">\n"
    + "<TR>\n"
    + "<TD COLSPAN=2 BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">\n"
    + "<A NAME=\"navbar_bottom_firstrow\"><!-- --></A>\n"
    + "<TABLE BORDER=\"0\" CELLPADDING=\"0\" CELLSPACING=\"3\" SUMMARY=\"\">\n"
    + "  <TR ALIGN=\"center\" VALIGN=\"top\">\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"package-summary.html\"><FONT CLASS=\"NavBarFont1\"><B>Package</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1Rev\"> &nbsp;<FONT CLASS=\"NavBarFont1Rev\"><B>Class</B></FONT>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"package-tree.html\"><FONT CLASS=\"NavBarFont1\"><B>Tree</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"deprecated-list.html\"><FONT CLASS=\"NavBarFont1\"><B>Deprecated</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"index-all.html\"><FONT CLASS=\"NavBarFont1\"><B>Index</B></FONT></A>&nbsp;</TD>\n"
    + "  <TD BGCOLOR=\"#EEEEFF\" CLASS=\"NavBarCell1\">    <A HREF=\"help-doc.html\"><FONT CLASS=\"NavBarFont1\"><B>Help</B></FONT></A>&nbsp;</TD>\n"
    + "  </TR>\n"
    + "</TABLE>\n"
    + "</TD>\n"
    + "<TD ALIGN=\"right\" VALIGN=\"top\" ROWSPAN=3><EM>\n"
    + "<TD BGCOLOR=#FFFFFF CLASS=\"NavBarCell1Rev\">  <A HREF='property-list.html'><FONT CLASS=NavBarFont1Rev><B>Properties</B></FONT></EM>\n"
    + "</TD>\n"
    + "</TR>\n"
//    + "<TD BGCOLOR=\"white\" CLASS=\"NavBarCell2\"><FONT SIZE=\"-2\">\n"
//    + "  <A HREF=\"index.html?Test.html\" target=\"_top\"><B>FRAMES</B></A>  &nbsp;\n"
//    + "&nbsp;<A HREF=\"Test.html\" target=\"_top\"><B>NO FRAMES</B></A>  &nbsp;\n"
    + "&nbsp;<SCRIPT type=\"text/javascript\">\n"
    + "  <!--\n"
    + "  if(window==top) {\n"
    + "    document.writeln('<A HREF=\"allclasses-noframe.html\"><B>All Classes</B></A>');\n"
    + "  }\n"
    + "  //-->\n"
    + "</SCRIPT>\n"
    + "<NOSCRIPT>\n"
    + "  <A HREF=\"allclasses-noframe.html\"><B>All Classes</B></A>\n"
    + "</NOSCRIPT>\n"
    + "\n"
    + "\n"
    + "</FONT></TD>\n"
    + "</TR>\n"
    + "</TABLE>\n"
    + "<A NAME=\"skip-navbar_bottom\"></A>\n"
    + "<!-- ======== END OF BOTTOM NAVBAR ======= -->\n"
    + "\n"
    + "<HR>\n"
    + "\n"
    + "</BODY>\n"
    + "</HTML>\n";

   public static void main(String[] args){
     String properties_dir = args[0] + File.separator + "__properties";
     File propertiesList   = new File(properties_dir + File.separator + "property-list.html");

     File undecidedStats   = new File(properties_dir + File.separator + "undecided.stats"); 
     File descriptiveStats = new File(properties_dir + File.separator + "descriptive.stats"); 
     File informalStats    = new File(properties_dir + File.separator + "informal.stats"); 
     File formalStats      = new File(properties_dir + File.separator + "formal.stats"); 

     try {
       (new PrintStream(new FileOutputStream(new File(args[0] + File.separator + "stylesheet.css"), true)))
         .println( ".Red { background-color:#EE0000; color:#FFFFFF }" +
             "\np { color:inherit;background-color:inherit }");
     } catch (java.io.IOException e){
        throw new RuntimeException(e);
     }

     int[] stats;

     stats = getStats(undecidedStats);
     int undecidedC   = stats[0]; 
     int undecidedW   = stats[1];  
     int undecidedL   = stats[2];

     stats = getStats(descriptiveStats);
     int descriptiveC = stats[0];
     int descriptiveW = stats[1];
     int descriptiveL = stats[2];

     stats = getStats(informalStats);
     int informalC    = stats[0];   
     int informalW    = stats[1];   
     int informalL    = stats[2];

     stats = getStats(formalStats);
     int formalC      = stats[0];     
     int formalW      = stats[1];     
     int formalL      = stats[2];     

     float totalC = undecidedC + descriptiveC + informalC + formalC;
     float totalW = undecidedW + descriptiveW + informalW + formalW;
     float totalL = undecidedL + descriptiveL + informalL + formalL;

     StringBuilder table 
       = new StringBuilder();
     table.append("<H2> MOP Coverage Statistics and Property Links</H2><HR />");
     table.append("<TABLE BORDER=\"1\" WIDTH=\"100%\" CELLPADDING=\"3\" CELLSPACING=\"0\" SUMMARY=\"\">");
     table.append("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\">");
     table.append("<TH ALIGN=\"left\" COLSPAN=\"4\"><FONT SIZE=\"+2\">");
     table.append("<B>MOP Definitional Statistics</B></FONT></TH></TR>");

     table.append(formatStat("Undecided Text",  
                              undecidedC,           totalC, 
                              undecidedW,           totalW, 
                              undecidedL,           totalL));
     table.append(formatStat("Descriptive Text",
                              descriptiveC,         totalC, 
                              descriptiveW,         totalW, 
                              descriptiveL,         totalL));
     table.append(formatStat("Property Text",   
                              informalC + formalC,  totalC, 
                              informalW + formalW,  totalW, 
                              informalL + formalL,  totalL));
     table.append(formatStat("Formalized Property Text",
                              formalC,              totalC, 
                              formalW,              totalW, 
                              formalL,              totalL));
     
     table.append("<BR /><BR /><TABLE BORDER=\"1\" WIDTH=\"100%\" CELLPADDING=\"3\" CELLSPACING=\"0\" SUMMARY=\"\">");
     table.append("<TR BGCOLOR=\"#CCCCFF\" CLASS=\"TableHeadingColor\">");
     table.append("<TH ALIGN=\"left\" COLSPAN=\"1\"><FONT SIZE=\"+2\">");
     table.append("<B>MOP Property Links</B></FONT></TH></TR>");

     for(String fn : 
         (new File(args[0])).list(new FilenameFilter() {
                                      public boolean accept(File dir, String name){
                                         return name.endsWith(".html") && !(name.equals("property-list.html"));
                                      } 
                                    })
         ){
         table.append("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\"><TD WIDTH=\"15%\">");
         table.append("<B><A HREF='");
         table.append(fn);
         table.append("'>");
         table.append(chop(fn)); 
         table.append("</A></B></TD></TR>"); 
     } 

     try {
       FileOutputStream fos = new FileOutputStream(propertiesList);
       PrintStream ps = new PrintStream(fos);
       ps.println(HTMLHEADER);
       ps.println(table);
       ps.println(HTMLFOOTER); 
     } catch (java.io.IOException e){
       throw new RuntimeException("Cannot create properties-list.html?");
     }
   }

   //Why can't java just have multiple return values?
   private static int[] getStats(File file){
     int [] ret = new int[3];
     try {
       FileInputStream fis = new FileInputStream(file);
       ObjectInputStream ois = new ObjectInputStream(fis);
       ret[0] = ois.readInt();
       ret[1] = ois.readInt();
       ret[2] = ois.readInt();
       return ret;
     } catch (java.io.IOException e){
       //if there is an IOException we assume that the file isn't there which means the tag wasn't seen
       ret[0] = 0;
       ret[1] = 0;
       ret[2] = 0;
       return ret;
     }
   }

   private static StringBuilder formatStat(String name, int c, float totalC, 
                                                        int w, float totalW, 
                                                        int l, float totalL){
     StringBuilder ret = new StringBuilder();
     ret.append("<TR BGCOLOR=\"white\" CLASS=\"TableRowColor\">");
     ret.append("<TD WIDTH=\"15%\">");
     ret.append(name);
     ret.append("</TD>");
     ret.append("<TD WIDTH=\"15%\">Characters: ");
     ret.append(c);
     ret.append(", ");
     ret.append((c / totalC) * 100f);
     ret.append("%</TD>");
     ret.append("<TD WIDTH=\"15%\">Words: ");
     ret.append(w);
     ret.append(", ");
     ret.append((w / totalW) * 100f);
     ret.append("%</TD>");
     ret.append("<TD WIDTH=\"15%\">Lines: ");
     ret.append(l);
     ret.append(", ");
     ret.append((l / totalL) * 100f);
     ret.append("%</TD></TR>");
     return ret;
  }

   private static String chop(String name){
     StringBuilder ret = new StringBuilder();
     String[] parts = name.split("[.]");
     for(int i = 0; i < parts.length - 1; ++i){
       ret.append(parts[i]);
     }
     return ret.toString();
   }
}
