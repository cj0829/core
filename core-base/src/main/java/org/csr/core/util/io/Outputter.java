package org.csr.core.util.io;

import java.io.IOException;
import java.io.OutputStream;

public abstract interface Outputter
{
  public abstract OutputStream getOutputStream(String paramString1, String paramString2, String paramString3)
    throws IOException;

  public abstract OutputStream getOutputStream(String paramString)
    throws IOException;
}

