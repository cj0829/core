/*package org.csr.core.persistence.generator4;

import java.io.Serializable;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;

*//**
 * ClassName:KeyGenerator.java <br/>
 * System Name：  <br/>
 * Date: 2014年10月17日上午11:46:39 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 *//*
public class GlobalGenerator implements PersistentIdentifierGenerator, Configurable {
	
	GlobalSequence sequence=null;
	@Override
	public Object generatorKey() {
		return sequence.getTableName();
	}
	public void configure(Type type, Properties params, Dialect dialect) throws MappingException {
		sequence=GlobalSequence.getSequence();
	}

	@Override
	public Serializable generate(final SessionImplementor session, final Object obj) {
		return sequence.generate(session);
	}

	@Override
	public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
		return new String[] { new StringBuilder()
				.append(dialect.getCreateTableString())
				.append(' ')
				.append(sequence.getTableName())
				.append(" ( ")
				.append(sequence.getSegmentColumnName())
				.append(' ')
				.append(dialect.getTypeName(Types.VARCHAR, sequence.getSegmentValueLength(),0, 0)).append(" not null ").append(",  ")
				.append(sequence.getValueColumnName()).append(' ')
				.append(dialect.getTypeName(Types.BIGINT))
				.append(", primary key ( ").append(sequence.getSegmentColumnName())
				.append(" ) )").append(dialect.getTableTypeString()).toString() };
	}
	@Override
	public String[] sqlDropStrings(Dialect dialect) throws HibernateException {
		return new String[] { dialect.getDropTableString(sequence.getTableName()) };
	}
}
*/