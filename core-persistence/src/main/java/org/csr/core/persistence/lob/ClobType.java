package org.csr.core.persistence.lob;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

public class ClobType implements UserType{

	@Override
	public int[] sqlTypes() {
		return new int[]{Types.CLOB};
	}

	@Override
	public Class<Clob> returnedClass() {
		return Clob.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return ( x == y ) || ( x != null && x.equals( y ) );
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,SessionImplementor session, Object owner) throws HibernateException, SQLException {
		Reader reader = rs.getCharacterStream( names[0] );
		if ( reader == null ) return null;
		StringBuilder result = new StringBuilder( 4096 );
		try {
			char[] charbuf = new char[4096];
			for ( int i = reader.read( charbuf ); i > 0 ; i = reader.read( charbuf ) ) {
				result.append( charbuf, 0, i );
			}
		}
		catch (IOException e) {
			throw new SQLException( e.getMessage() );
		}
		return result.toString();
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,SessionImplementor session) throws HibernateException, SQLException {
		if ( value != null ) {
			String string = (String) value;
			StringReader reader = new StringReader( string );
			st.setCharacterStream( index, reader, string.length() );
		}
		else {
			st.setNull( index, sqlTypes()[0] );
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}
}
