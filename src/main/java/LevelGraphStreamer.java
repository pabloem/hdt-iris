import java.io.*;
import java.util.*;
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
    pr = Runtime.getRuntime().exec(cmd);
    stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
    stdIn = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
  }
  public void preCache() {
    // Pre-caches the contents of a levelgraph database to a Bloom filter
    // TODO implement
  }
  public void finalize() {
    stdOut.close();
    stdIn.close();
  }
  public boolean contains(String triple) {
    Pattern pattern = Pattern.compile("^(?:<([^>]+)>\s*){2}<?([^>]+)>?$");
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
    String line = stdOut.readLine();
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
