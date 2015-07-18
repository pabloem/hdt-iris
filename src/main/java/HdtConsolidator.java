/*
 * */
package pablo;

import java.io.*;
import java.util.*;

final String levelStreamer = "./levelStreamer.js";
final String rdf2hdt = "./rdf2hdt";

class HdtConsolidator {
  public HdtConsolidator(String added, String removed, 
                         String hdt, String output) {
    String cmdAdded = levelStreamer + " " + added;
    Process p = Runtime.getRuntime().exec(cmdAdded);

    BufferedReader addedInp = new BufferedReader( new InputStreamReader(p.getInputStream()) );
    BufferedReader addedOut = new BufferedWriter( new OutputStreamWriter(p.getOutputStream()) );

    String line = "";			
    while ((line = addedInp.readLine())!= null) {
      sb.append(line + "\n");
    }
  }
};
