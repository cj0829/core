package org.csr.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class 基础处理对象。
 * @author caijin
 *
 */
public class ClassUtil {

	private ClassUtil() {
		// hide default constructor to prevent instantiation
	}
	private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);
	/**
	 * Maps wrapper <code>Class</code>es to their corresponding primitive types.
	 */
	private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = createWrapperPrimitiveMap();

	private static Map<Class<?>, Class<?>> createWrapperPrimitiveMap() {
		Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
		map.put(Boolean.class, boolean.class);
		map.put(Byte.class, byte.class);
		map.put(Character.class, char.class);
		map.put(Short.class, short.class);
		map.put(Integer.class, int.class);
		map.put(Long.class, long.class);
		map.put(Double.class, double.class);
		map.put(Float.class, float.class);
		return map;
	}
	public static  Object getObjectValue(Field field,String value){
		if(StringUtils.isBlank(value)){
			return null;
		}
		if(Boolean.TYPE == field.getType()){
			return Boolean.parseBoolean(value);
		}
		if(Integer.TYPE == field.getType()){
			return Integer.parseInt(value);
		}
		if(Float.TYPE == field.getType()){
			return Float.parseFloat(value);
		}
		if(Double.TYPE == field.getType()){
			return Double.parseDouble(value);
		}
		if(Character.class == field.getType()){
			return value.charAt(0);
		}
		if(String.class == field.getType()){
			return value;
		}
		return null;
	}
	/**
	 * findField: 在给定的类，并在其父类搜索领域  <br/>
	 * @author caijin
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @since JDK 1.7
	 */
	public static Field findField(Class<?> clazz, String fieldName) {
		return findField(clazz, fieldName, clazz);
	}

	private static Field findField(Class<?> clazz, String fieldName,Class<?> original) {
		Field field = null;

		try {
			field = clazz.getDeclaredField(fieldName);
			if (log.isTraceEnabled())
				log.trace("found field " + fieldName + " in " + clazz.getName());
		} catch (NoSuchFieldException e) {
			if (clazz.getSuperclass() != null) {
				return findField(clazz.getSuperclass(), fieldName, original);
			} else {
				throw new RuntimeException("couldn't find field '"
						+ original.getName() + "." + fieldName + "'", e);
			}
		}
		return field;
	}

	/**
	 * getMethod: 搜索在给定的类，并在其父类的方法 <br/>
	 * @author caijin
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @since JDK 1.7
	 */
	public static Method getMethod(Class<?> clazz, String methodName,Class<?>[] parameterTypes) {
		return getMethod(clazz, methodName, parameterTypes, clazz);
	}

	private static Method getMethod(Class<?> clazz, String methodName,Class<?>[] parameterTypes, Class<?> original) {
		Method method = null;

		try {
			method = clazz.getDeclaredMethod(methodName, parameterTypes);

			if (log.isTraceEnabled())
				log.trace("found method " + clazz.getName() + "." + methodName+ "(" + Arrays.toString(parameterTypes) + ")");

		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() != null) {
				return getMethod(clazz.getSuperclass(), methodName,parameterTypes, original);
			} else {
				throw new RuntimeException("couldn't find method '"
						+ original.getName() + "." + methodName + "("
						+ getParameterTypesText(parameterTypes) + ")'", e);
			}
		}

		return method;
	}

	private static String getParameterTypesText(Class<?>[] parameterTypes) {
		if (parameterTypes == null)
			return "";
		StringBuilder parameterTypesText = new StringBuilder();
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> parameterType = parameterTypes[i];
			parameterTypesText.append(parameterType.getName());
			if (i != parameterTypes.length - 1) {
				parameterTypesText.append(", ");
			}
		}
		return parameterTypesText.toString();
	}
	public static <T> T newInstance(String clazzName) {
		if (clazzName == null) {
			throw new IllegalArgumentException(
					"cannot create new instance without class");
		}
		Class<T> clas;
		try {
			clas =  (Class<T>) classForName(clazzName);
			return  newInstance(clas);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
	public static <T> T newInstance(Class<T> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException(
					"cannot create new instance without class");
		}
		try {
			return newInstance(clazz.getConstructor());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"cannot instantiate class without default constructor");
		}
	}

	public static <T> T newInstance(Constructor<T> constructor, Object... args) {
		if (constructor == null) {
			throw new IllegalArgumentException(
					"cannot create new instance without constructor");
		}

		Class<T> clazz = constructor.getDeclaringClass();
		if (log.isTraceEnabled())
			log.trace("creating new instance for " + clazz + " with args "
					+ Arrays.toString(args));
		if (!constructor.isAccessible()) {
			if (log.isTraceEnabled())
				log.trace("making constructor accessible");
			constructor.setAccessible(true);
		}
		try {
			return constructor.newInstance(args);
		} catch (InstantiationException e) {
			throw new RuntimeException("failed to instantiate " + clazz, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(ClassUtil.class + " has no access to "
					+ constructor, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(constructor + " threw exception",
					e.getCause());
		}
	}

	public static Object get(Field field, Object object) {
		if (field == null) {
			throw new NullPointerException("field is null");
		}
		try {
			Object value = field.get(object);
			if (log.isTraceEnabled())
				log.trace("got value '" + value + "' from field '"+ field.getName() + "'");
			return value;
		} catch (Exception e) {
			throw new RuntimeException(
					"couldn't get '" + field.getName() + "'", e);
		}
	}

	/**
	 * 设置对象值
	 * @param field
	 * @param object
	 * @param value
	 */
	public static void set(Field field, Object object, Object value) {
		if (field == null) {
			throw new NullPointerException("field is null");
		}
		try {
			if (log.isTraceEnabled())
				log.trace("setting field '" + field.getName() + "' to value '"+ value + "'");
			if (!field.isAccessible()) {
				if (log.isTraceEnabled())
					log.trace("making field accessible");
				field.setAccessible(true);
			}
			field.set(object, value);
		} catch (Exception e) {
			throw new RuntimeException("couldn't set '" + field.getName()+ "' to '" + value + "'", e);
		}
	}

	/**
	 * 执行方法
	 * @param method
	 * @param target
	 * @param args
	 * @return
	 */
	public static Object invoke(Method method, Object target, Object... args) {
		if (method == null) {
			throw new RuntimeException("method is null");
		}
		try {
			if (log.isTraceEnabled())
				log.trace("invoking '" + method.getName() + "' on '" + target+ "' with " + Arrays.toString(args));
			if (!method.isAccessible()) {
				log.trace("making method accessible");
				method.setAccessible(true);
			}
			return method.invoke(target, args);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			throw new RuntimeException("couldn't invoke '" + method.getName()+ "' with " + Arrays.toString(args) + " on " + target+ ": " + targetException.getMessage(), targetException);
		} catch (Exception e) {
			throw new RuntimeException("couldn't invoke '" + method.getName()+ "' with " + Arrays.toString(args) + " on " + target+ ": " + e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Checks if the given <code>value</code> can be assigned to a variable of
	 * the specified <code>type</code>.
	 * </p>
	 * <p>
	 * Unlike the {@link Class#isAssignableFrom(Class)} method, this method
	 * takes into account widenings of primitive types and <code>null</code>s.
	 * </p>
	 * <p>
	 * Primitive widenings allow an int to be assigned to a long, float or
	 * double. This method returns the correct result for these cases.
	 * </p>
	 * <p>
	 * <code>null</code> may be assigned to any reference type. This method will
	 * return <code>true</code> if <code>null</code> is passed in and the
	 * specified <code>type</code> is a reference type.
	 * </p>
	 * <p>
	 * Specifically, this method tests whether the class of the given
	 * <code>value</code> parameter can be converted to the type represented by
	 * the specified <code>Class</code> via an identity, widening primitive or
	 * widening reference conversion. See the <a
	 * href="http://java.sun.com/docs/books/jls/">Java Language
	 * Specification</a>, sections 5.1.1, 5.1.2 and 5.1.4 for details.
	 * </p>
	 * 
	 * @param type
	 *            the Class to try to assign into
	 * @param value
	 *            the object to check, may be <code>null</code>
	 * @return <code>true</code> if assignment is possible
	 * @see <a
	 *      href="http://commons.apache.org/lang/api-release/org/apache/commons/lang/ClassUtils.html#isAssignable(java.lang.Class,%20java.lang.Class)"
	 *      >ClassUtils.isAssignable()</a>
	 */
	private static boolean isAssignable(Class<?> type, Object value) {
		// check for null value
		if (value == null) {
			// null is assignable to reference types
			return !type.isPrimitive();
		}

		if (type.isPrimitive()) {
			// unboxing
			Class<?> valueType = wrapperToPrimitive(value.getClass());
			if (null == valueType) {
				return false;
			}
			if (type == valueType) {
				return true;
			}
			// widening primitive conversion
			if (int.class == valueType) {
				return long.class == type || float.class == type
						|| double.class == type;
			}
			if (long.class == valueType) {
				return float.class == type || double.class == type;
			}
			if (boolean.class == valueType) {
				return false;
			}
			if (double.class == valueType) {
				return false;
			}
			if (float.class == valueType) {
				return double.class == type;
			}
			if (char.class == valueType) {
				return int.class == type || long.class == type
						|| float.class == type || double.class == type;
			}
			if (short.class == valueType) {
				return int.class == type || long.class == type
						|| float.class == type || double.class == type;
			}
			if (byte.class == valueType) {
				return short.class == type || int.class == type
						|| long.class == type || float.class == type
						|| double.class == type;
			}
			// should never get here
			return false;
		}

		return type.isInstance(value);
	}

	/**
	 * <p>
	 * Converts the specified wrapper class to its corresponding primitive
	 * class.
	 * </p>
	 * <p>
	 * If the passed in class is a wrapper class for a primitive type, this
	 * primitive type will be returned (e.g. <code>Integer.TYPE</code> for
	 * <code>Integer.class</code>). For other classes, or if the parameter is
	 * <code>null</code>, the return value is <code>null</code>.
	 * </p>
	 * 
	 * @param cls
	 *            the class to convert, may be <code>null</code>
	 * @return the corresponding primitive type if <code>cls</code> is a wrapper
	 *         class, <code>null</code> otherwise
	 * @see <a
	 *      href="http://commons.apache.org/lang/api-release/org/apache/commons/lang/ClassUtils.html#wrapperToPrimitive(java.lang.Class)"
	 *      >ClassUtils.wrapperToPrimitive</a>
	 */
	private static Class<?> wrapperToPrimitive(Class<?> cls) {
		return wrapperPrimitiveMap.get(cls);
	}

	public static void uninstallDeploymentClassLoader(ClassLoader original) {
		if (original != null) {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	/**
	 * Perform resolution of a class name.
	 * <p/>
	 * Same as {@link #classForName(String, Class)} except that here we delegate
	 * to {@link Class#forName(String)} if the context classloader lookup is
	 * unsuccessful.
	 * 
	 * @param name
	 *            The class name
	 * @return The class reference.
	 * @throws ClassNotFoundException
	 *             From {@link Class#forName(String)}.
	 */
	public static Class<?> classForName(String name) throws ClassNotFoundException {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		if (contextClassLoader != null) {
			try {
				return Class.forName(name, true, contextClassLoader);
			} catch (ClassNotFoundException e) {
				// keep going to load through the loader of the current class
			}
		}
		return Class.forName(name);
	}
}
