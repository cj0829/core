package org.csr.core.cloudsession;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;


public class JavaSerializer implements Serializer {
	@SuppressWarnings("unused")
	private ClassLoader loader;
	@Override
	public void setClassLoader(ClassLoader loader) {
		this.loader = loader;
	}

	public byte[] attributesHashFrom(CloudSession session) throws IOException {
		ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
//		for (Enumeration<String> enumerator = session.getAttributeNames(); enumerator.hasMoreElements();) {
//			String key = enumerator.nextElement();
//			attributes.put(key, session.getAttribute(key));
//		}
		byte[] serialized = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));) {
			oos.writeUnshared(attributes);
			oos.flush();
			serialized = bos.toByteArray();
		}
		MessageDigest digester = null;
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		return digester.digest(serialized);
	}

	@Override
	public byte[] serializeFrom(CloudSession session,
			SessionSerializationMetadata metadata) throws IOException {
		byte[] serialized = null;

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos));) {
			oos.writeObject(metadata);
			session.doWriteObject(oos);
			oos.flush();
			serialized = bos.toByteArray();
		}
		return serialized;
	}

	@Override
	public void deserializeInto(byte[] data, CloudSession session,
			SessionSerializationMetadata metadata) throws IOException,
			ClassNotFoundException {
		try (BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(data));ObjectInputStream ois = new ObjectInputStream(bis);) {
			SessionSerializationMetadata serializedMetadata = (SessionSerializationMetadata) ois.readObject();
			metadata.copyFieldsFrom(serializedMetadata);
			session.doReadObject(ois);
		}
	}
}
