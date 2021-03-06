
package com.consolidator;

import java.io.*;
import java.util.*;

class Reader implements Runnable
{
public Reader (InputStream istrm, OutputStream ostrm) {
      this.istrm = istrm;
      this.ostrm = ostrm;
  }
  public void run() {
      try
      {
          final byte[] buffer = new byte[1024];
          for (int length = 0; (length = istrm.read(buffer)) != -1; )
          {
              ostrm.write(buffer, 0, length);
          }
      }
      catch (Exception e)
      {
              //e.printStackTrace();
      }
  }
  private final OutputStream ostrm;
  private final InputStream istrm;
}
