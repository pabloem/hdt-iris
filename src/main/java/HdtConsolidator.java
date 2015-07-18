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
    addSourceHdt();
    appendAdded();
  }

  private void addSourceHdt() {
    String cmdHdt = hdtSearch+ " "+hdt_in;
    LevelGraphStreamer rmvDb = new LevelGraphStreamer(removed_db);
    rmvDb.init();
    rmvDb.preCache();
    try {
      Process p = Runtime.getRuntime().exec(cmdHdt);
      BufferedReader addedOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = "";			
      while ((line = lgAdd.getTriple())!= null) {
        if(rmvDb.contains(line)) continue;
        appendToHdt(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void appendAdded() {
    LevelGraphStreamer lgAdd = new LevelGraphStreamer(added_db);
    lgAdd.init();
    lgAdd.startQuery("","","");
    String line = "";			
    while ((line = lgAdd.getTriple())!= null) {
      appendToHdt(line);
    }
    lgAdd.finalize();
  }

  private void appendToHdt(String triple) {
    // TODO - implement. Add line to HDT file.
    System.out.println(triple);
  }
};
