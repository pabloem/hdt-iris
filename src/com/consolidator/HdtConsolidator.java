/*
 * */
package com.consolidator;
import java.io.*;
import java.util.*;

public class HdtConsolidator {
  public static final String defaultRdf2hdt = "./rdf2hdt -f ntriples";
  public static final String defaultHdt2rdf= "./hdt2rdf";
  public static final String defaultLevelStreamer = "./levelStreamerLine.js";

  private String added_db;       // Our levelgraph with added elements
  private String removed_db;     // Our levelgraph with removed elements
  private String hdt_in;         // The HDT input file
  private String hdt_out;        // The resulting HDT file
  private BufferedWriter fifoToHdt;   // Our intermediate FIFO
  private BufferedWriter testFile;
  private BufferedReader fifoFromHdt; // Our intermediate FIFO
  private String fifoFileToHdt;
  private int addedTriples;

  private String rdf2hdt;
  private String hdt2rdf;
  private String levelStreamer;

  public HdtConsolidator(String added, String removed, 
                         String hdt, String output) {
    added_db = added;
    removed_db = removed;
    hdt_in = hdt;
    hdt_out = output;
    loadConfiguration();
  }

  private void loadConfiguration() {
    Properties prop = new Properties();
    InputStream input = null;
    try {
      String filename = "config.properties";
      input = getClass().getClassLoader().getResourceAsStream(filename);
      if(input == null) {
        defaultConfiguration();
        return ;
      }
      prop.load(input);
    } catch (IOException ex) {
      defaultConfiguration();
    }
    levelStreamer = prop.getProperty("levelStreamer");
    hdt2rdf = prop.getProperty("hdt2rdf");
    rdf2hdt = prop.getProperty("rdf2hdt");
  }

  private void defaultConfiguration() {
    System.out.println("Unable to find config.properties. Using default values.");
    rdf2hdt = defaultRdf2hdt;
    hdt2rdf = defaultHdt2rdf;
    levelStreamer = defaultLevelStreamer;
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
    return fifoFile;
  }

  public void setupRun() throws Exception {
    /* First, we create out 'named pipe', or fifo for generating HDT file */
    fifoFileToHdt = makeFifo();

    /* Second, we start executing the rdf2hdt process */
    String rdf2hdtCmd = rdf2hdt + " " + fifoFileToHdt + " " + hdt_out;
    Process p = Runtime.getRuntime().exec(rdf2hdtCmd);
    System.out.println("Executing: "+rdf2hdtCmd);

    /* Third, we get a write stream to our named pipe  */
    fifoToHdt = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fifoFileToHdt)));
    //testFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testFile.rdf")));
    
    /* Fourth, we generate another 'named pipe' to read in our HDT file,
       and we get its read stream. */
    String fifoFileFromHdt = makeFifo();
    String cmdHdt = hdt2rdf+ " "+hdt_in+" "+fifoFileFromHdt;
    Process pr = Runtime.getRuntime().exec(cmdHdt);
    System.out.println("Executing: "+cmdHdt);
    fifoFromHdt = new BufferedReader(new InputStreamReader(new FileInputStream(fifoFileFromHdt)));
    System.out.println("Ready to go!");
    return ;
  }

  public void finalize() throws IOException {
    System.out.println("Finalizing.");
    fifoToHdt.close();
    //testFile.close();
    fifoFromHdt.close();
  }

  public void run() throws Exception {
    setupRun();
    addSourceHdt();
    appendAdded();
    finalize();
    finalizingHack();
  }

  /* HACK. I am using this because simply closing the buffered writer is not working */
  private void finalizingHack() throws Exception {
    ProcessBuilder pb = new ProcessBuilder(new String[]{"bash", "-c", "echo . >> "+fifoFileToHdt});
    Process p = pb.start();
    p.waitFor();
  }

  private void addSourceHdt() {
    String line = "";
    LevelGraphStreamer lgRmv = new LevelGraphStreamer(removed_db,levelStreamer);
    lgRmv.init();
    lgRmv.preCache();
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
    LevelGraphStreamer lgAdd = new LevelGraphStreamer(added_db,levelStreamer);
    lgAdd.init();
    lgAdd.startQuery("");
    String line = "";			
    while ((line = lgAdd.getTriple())!= null) {
      appendToHdt(line);
    }
    lgAdd.finalize();
  }

  private void appendToHdt(String triple) {
    addedTriples += 1;
    if(addedTriples % 100000 == 0){
      System.out.println("Added "+addedTriples+" triples.");
    }
    try {
      fifoToHdt.write(triple+"\n");
      //testFile.write(triple+"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
};
