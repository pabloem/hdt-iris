/*
 * */
import java.io.*;
import java.util.*;

public class HdtConsolidator {
  public static final String rdf2hdt = "../../../rdf2hdt";
  public static final String levelStreamer = "../../../levelStreamerLine.js";
  public static final String hdtSearch = "../../../hdtSearch";

  private String added_db;       // Our levelgraph with added elements
  private String removed_db;     // Our levelgraph with removed elements
  private String hdt_in;         // The HDT input file
  private String hdt_out;        // The resulting HDT file
  private BufferedWriter fifo;   // Our intermediate FIFO

  public HdtConsolidator(String added, String removed, 
                         String hdt, String output) {
    added_db = added;
    removed_db = removed;
    hdt_in = hdt;
    hdt_out = output;
  }

  private String generateFifoName(int len) {
    String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
    Random rnd = new Random();
    StringBuilder sb = new StringBuilder( len );
    for( int i = 0; i < len; i++ ) 
      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
    return sb.toString();
  }

  public void setupRun() throws Exception {
    /* First, we create out 'named pipe', or fifo 
    String fifoFile = "/tmp/"+generateFifoName(6);
    String mkFifoCmd = "mkfifo "+fifoFile;
    Process p Runtime.getRuntime().exec(mkFifoCmd);
    int res = p.waitFor();*/

    /* Second, we get a write stream to our named pipe 
       fifo = new BufferedWriter(new BufferedOutputStream(new FileOutputStream(fifoFile)));*/
    
    /* Third, we start executing the rdf2hdt process 
    String rdf2hdtCmd = rdf2hdt + " " + fifoFile + " " + hdt_out;
    Process p Runtime.getRuntime().exec(rdf2hdtCmd);*/

    return ;
  }

  public void finalize() {
    /*fifo.newLine();
      fifo.close();*/
  }

  public void run() throws Exception {
    String cmdRemoved = levelStreamer + " " + removed_db;
    setupRun();
    addSourceHdt();
    appendAdded();
    finalize();
  }

  private void addSourceHdt() {
    String cmdHdt = hdtSearch+ " "+hdt_in;
    LevelGraphStreamer lgRmv = new LevelGraphStreamer(removed_db);
    lgRmv.init();
    lgRmv.preCache();
    try {
      Process p = Runtime.getRuntime().exec(cmdHdt);
      BufferedReader addedOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = "";			
      while ((line = addedOut.readLine())!= null) {
        if(lgRmv.contains(line)) continue;
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
