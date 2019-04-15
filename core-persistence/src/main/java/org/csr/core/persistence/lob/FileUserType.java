package org.csr.core.persistence.lob;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.compare.EqualsHelper;
import org.hibernate.usertype.UserType;

/**
 * 这个一个附件的自定义的hibernate 的数据类型，提供，文件的上传或者下载操作。
 * 
 * @author caijin
 *
 */
public class FileUserType implements UserType {

	@Override
	public int[] sqlTypes() {
		System.out.println("public int[] sqlTypes() {");
		return  new int[] {Types.CLOB};
	}

	@Override
	public Class returnedClass() {
		return String.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		System.out.println("public boolean equals(Object x, Object y) throws HibernateException");
		return EqualsHelper.equals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		System.out.println("public int hashCode(Object x) throws HibernateException");
		return 0;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		System.out.println("public Object nullSafeGet(ResultSet rs, String[] names,");
		return "asdfasdfasdf";
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		System.out.println("public void nullSafeSet(PreparedStatement st, Object value, int index,");
		st.setString(index, "asdfasdfasfa-------------dddd");
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		System.out.println("public Object deepCopy(Object value) throws HibernateException");
		return value;
	}

	@Override
	public boolean isMutable() {
		System.out.println("public boolean isMutable()");
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		System.out.println("public Serializable disassemble(Object value) throws HibernateException");
		return (Serializable) value;
		
	}

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		System.out.println("public Object assemble(Serializable cached, Object owner)");
		return owner;
	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		System.out.println("public Object replace(Object original, Object target, Object owner)");
		return owner;
	}

}
