package org.csr.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
}
