/*
 * */
import java.io.*;
import java.util.*;

public class HdtConsolidator {
  public static final String rdf2hdt = "./rdf2hdt";
  public static final String levelStreamer = "./levelStreamer.js";
  public HdtConsolidator(String added, String removed, 
                         String hdt, String output) {
    String cmdAdded = levelStreamer + " " + added;
    try {
      Process p = Runtime.getRuntime().exec(cmdAdded);

      BufferedReader addedInp = new BufferedReader( new InputStreamReader(p.getInputStream()) );
      BufferedWriter addedOut = new BufferedWriter( new OutputStreamWriter(p.getOutputStream()) );
      System.out.println("Hi testing");

      String line = "";			
      while ((line = addedInp.readLine())!= null) {
        System.out.println(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
};
