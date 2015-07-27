import java.io.*;
import java.util.*;
import com.consolidator.*;

public class consolidate {
  public static void main(String[] args) throws Exception {
    HdtConsolidator hc = new HdtConsolidator("../../../workspace/added.db/","../../../workspace/removed.db/","../../../test.hdt","hdtRes.hdt");
    hc.run();
  }
}
