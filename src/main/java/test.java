import java.io.*;
import java.util.*;

public class test {
  public static void main(String[] args) throws Exception {
    HdtConsolidator hc = new HdtConsolidator("../../../workspace/added.db/","../../../workspace/removed.db/","../../../test.hdt","hdtRes.hdt");
    hc.run();
  }
}
