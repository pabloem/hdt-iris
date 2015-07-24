/*
 * */
import java.io.*;
import java.util.*;

public class HdtConsolidator {
  public static final String rdf2hdt = "../../../rdf2hdt";
  public static final String levelStreamer = "../../../levelStreamerLine.js";
  public static final String hdt2rdf= "../../../hdt2rdf";

  private String added_db;       // Our levelgraph with added elements
  private String removed_db;     // Our levelgraph with removed elements
  private String hdt_in;         // The HDT input file
  private String hdt_out;        // The resulting HDT file
  private BufferedWriter fifoToHdt;   // Our intermediate FIFO
  private BufferedReader fifoFromHdt; // Our intermediate FIFO

  public HdtConsolidator(String added, String removed, 
                         String hdt, String output) {
    added_db = added;
    removed_db = removed;
    hdt_in = hdt;
    hdt_out = output;
  }

  private String generateFifoName(int len) {
    String AB = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random rnd = new Random();
    StringBuilder sb = new StringBuilder( len );
    for( int i = 0; i < len; i++ ) 
      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
    return "/tmp/hdtCon"+sb.toString();
  }

  private String makeFifo() throws Exception{
    String fifoFile = generateFifoName(6);
    String mkFifoCmd = "mkfifo "+fifoFile;
    Process p = Runtime.getRuntime().exec(mkFifoCmd);
    int res = p.waitFor();
    System.out.println("Fifo: "+fifoFile);
    return fifoFile;
  }

  public void setupRun() throws Exception {
    /* First, we create out 'named pipe', or fifo for generating HDT file */
    String fifoFileToHdt = makeFifo();

    /* Second, we start executing the rdf2hdt process */
    String rdf2hdtCmd = rdf2hdt + " " + fifoFileToHdt + " " + hdt_out;
    Process p = Runtime.getRuntime().exec(rdf2hdtCmd);
    System.out.println("Executing: "+rdf2hdtCmd);

    /* Third, we get a write stream to our named pipe  */
    fifoToHdt = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fifoFileToHdt)));
    
    /* Fourth, we generate another 'named pipe' to read in our HDT file,
       and we get its read stream. */
    String fifoFileFromHdt = makeFifo();
    String cmdHdt = hdt2rdf+ " "+hdt_in+" "+fifoFileFromHdt;
    Process pr = Runtime.getRuntime().exec(cmdHdt);
    fifoFromHdt = new BufferedReader(new InputStreamReader(new FileInputStream(fifoFileFromHdt)));
    return ;
  }

  public void finalize() throws IOException {
    fifoToHdt.newLine();
    fifoToHdt.close();
    fifoFromHdt.close();
  }

  public void run() throws Exception {
    setupRun();
    addSourceHdt();
    appendAdded();
    finalize();
  }

  private void addSourceHdt() {
    String line = "";
    LevelGraphStreamer lgRmv = new LevelGraphStreamer(removed_db);
    lgRmv.init();
    try {
    while((line = fifoFromHdt.readLine()) != null) {
      if(lgRmv.contains(line)) continue;
      appendToHdt(line);
    }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void appendAdded() {
    LevelGraphStreamer lgAdd = new LevelGraphStreamer(added_db);
    lgAdd.init();
    lgAdd.startQuery("");
    String line = "";			
    while ((line = lgAdd.getTriple())!= null) {
      appendToHdt(line);
    }
    lgAdd.finalize();
  }

  private void appendToHdt(String triple) {
    // TODO - implement. Add line to HDT file.
    System.out.println(triple);
    try {
      fifoToHdt.write(triple+"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
};
