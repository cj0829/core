package org.csr.core.cloudsession;

import java.io.IOException;

public interface Serializer {
  void setClassLoader(ClassLoader loader);

  byte[] attributesHashFrom(CloudSession session) throws IOException;
  byte[] serializeFrom(CloudSession session, SessionSerializationMetadata metadata) throws IOException;
  void deserializeInto(byte[] data, CloudSession session, SessionSerializationMetadata metadata) throws IOException, ClassNotFoundException;
}
