package com.consolidator;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import com.skjegstad.utils.BloomFilter;

class LevelGraphStreamer {
  private String levelStreamer;
  private String streamFile;
  private BufferedReader stdOut;
  private BufferedWriter stdIn;
  private Process pr;
  private BloomFilter<String> bf;
  private boolean cached;
  public LevelGraphStreamer(String file, String executable) {
    levelStreamer = executable;
    streamFile = file;
  }
  public void init() {
    cached = false;
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
    cached = true;
    bf = new BloomFilter<String>(3,1000000,3);
    startQuery("");
    String tr = "";
    while((tr = getTriple()) != null) {
      int cut = tr.length() - 1;
      if(tr.charAt(tr.length() - 2) == ' ') {
        cut -= 1;
      }
      tr = tr.substring(0,cut);
      bf.add(tr);
    }
  }
  public void finalize() {
    try {
      stdOut.close();
      stdIn.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private boolean bloomContains(String triple) {
    if(cached == false) return true;
    int cut = triple.length() - 1;
    if(triple.charAt(triple.length() - 2) == ' ') {
      cut -= 1;
    }
    triple = triple.substring(0,cut);
    return bf.contains(triple);
  }
  public boolean contains(String triple) {
    if(!bloomContains(triple)) return false;
    startQuery(triple);

    if(getTriple() == null) return false;
    getTriple(); // Get the last line
    return true;
  }
  public String getTriple() {
    String line = "";
    try {
      line = stdOut.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if(line == null || line.length() < 5) {
      // A string of less than 5 characters can not be a triple.
      return null;
    }
    return line;
  }
  public void startQuery(String query) {
    try{ 
      stdIn.write(query+"\n");
      stdIn.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void startQuery(String subject,String predicate,String object) {
    try{
      stdIn.write(subject+" "+predicate+" "+object+" .\n");
      stdIn.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
};
