/*
 * */
import java.io.*;
import java.util.*;

public class HdtConsolidator {
  public static final String rdf2hdt = "./rdf2hdt";
  public static final String levelStreamer = "../../../levelStreamerLine.js";
  public static final String hdtSearch = "./hdtSearch";

  private String added_db;
  private String removed_db;
  private String hdt_in;
  private String hdt_out;  

  public HdtConsolidator(String added, String removed, 
                         String hdt, String output) {
    added_db = added;
    removed_db = removed;
    hdt_in = hdt;
    hdt_out = output;
  }

  public void run() {
    String cmdRemoved = levelStreamer + " " + removed_db;
    appendAdded();
  }

  private void appendAdded() {
    String cmdAdded = levelStreamer + " " + added_db;
    try {
      Process p = Runtime.getRuntime().exec(cmdAdded);
      BufferedReader addedOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedWriter addedInp = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
      addedInp.newLine();
      addedInp.newLine();
      addedInp.newLine();
      String line = "";			
      while ((line = addedOut.readLine())!= null) {
        appendToHdt(line);
      }
      addedOut.close();
      addedInp.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void appendToHdt(String triple) {
    // TODO - implement. Add line to HDT file.
    System.out.println(triple);
  }
};
