import java.io.*;
import java.util.*;
import java.util.regex.*;

class LevelGraphStreamer {
  public static final String levelStreamer = "../../../levelStreamerLine.js";
  private String streamFile;
  private BufferedReader stdOut;
  private BufferedWriter stdIn;
  private Process pr;
  public LevelGraphStreamer(String file) {
    streamFile = file;
  }
  public void init() {
    String cmd = levelStreamer + " " + streamFile;
    try {
      pr = Runtime.getRuntime().exec(cmd);
      stdOut = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      stdIn = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void preCache() {
    // Pre-caches the contents of a levelgraph database to a Bloom filter
    // TODO implement
  }
  public void finalize() {
    try {
      stdOut.close();
      stdIn.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public boolean contains(String triple) {
    Pattern pattern = Pattern.compile("^<([^>]+)>\\s+<([^>]+)>\\s+<?([^>]+)>?$");
    Matcher matcher = pattern.matcher(triple);
    if(matcher.matches()) {
      System.out.print(matcher.group(1)+" "+matcher.group(2)+" "+matcher.group(3));
      startQuery(matcher.group(1),matcher.group(2),matcher.group(3));
    } else {
      /* Gotta error out */
    }
    if(getTriple() == null) return false;
    getTriple(); // Get the last line
    return true;
  }
  public String getTriple() {
    String line;
    try {
      line = stdOut.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(line.length() < 5) {
      // A string of less than 5 characters can not be a triple.
      return null;
    }
    return line;
  }
  public void startQuery(String subject,String predicate,String object) {
    try{ 
      stdIn.write(subject+"\n");
      stdIn.flush();
      stdIn.write(predicate+"\n");
      stdIn.flush();
      stdIn.write(object+"\n");
      stdIn.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
};
