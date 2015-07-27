import java.io.*;
import java.util.*;
import com.consolidator.*;

public class consolidate {
  public static void main(String[] args) throws Exception {
    if(args.length < 4) {
      System.out.println("Usage: consolidate.sh added removed hdt outputHdt");
      return ;
    }
    HdtConsolidator hc = new HdtConsolidator(args[0],args[1],args[2],args[3]);
    hc.run();
  }
}
