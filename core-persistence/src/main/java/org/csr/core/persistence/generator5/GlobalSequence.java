package org.csr.core.persistence.generator5;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.csr.core.util.ObjUtil;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.engine.spi.SessionEventListenerManager;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.jdbc.AbstractReturningWork;
import org.hibernate.type.LongType;
import org.jboss.logging.Logger;

/**
 * ClassName:Sequence.java <br/>
 * System Name：  <br/>
 * Date: 2014年10月17日下午12:06:41 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： <br/>
 *        公用方法描述： <br/>
 */
public class GlobalSequence {
	
	private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, GlobalSequence.class.getName());
	//
	private String tableName="pmt_tb_generator";

	private String segmentColumnName="sequence_name";
	private String segmentValue="default_global";
	//
	private String valueColumnName="sequence_next_hi_value";
	
	private int initialValue=1;
	private int incrementSize=200;
	
	private long accessCount = 0;

	Optimizer optimizer;
	
	private static GlobalSequence globalSequence;
	
	public GlobalSequence(){
		//需要先初始化
		optimizer=new KeyLoOptimizer(LongType.class, incrementSize);
	}
	
	public static GlobalSequence getSequence() {
		if(ObjUtil.isEmpty(globalSequence)){
			globalSequence=new GlobalSequence();
		}
		return globalSequence;
	}
	protected String buildSelectQuery() {
		final String alias = "tbl";
		String query = "select " + StringHelper.qualify(alias, valueColumnName)
				+ " from " + tableName + ' ' + alias + " where "
				+ StringHelper.qualify(alias, segmentColumnName) + "=? for update";
		return query;
	}
	
	protected String buildUpdateQuery() {
		return "update " + tableName + " set " + valueColumnName + "=? "
				+ " where " + valueColumnName + "=? and " + segmentColumnName
				+ "=?";
	}

	protected String buildInsertQuery() {
		return "insert into " + tableName + " (" + segmentColumnName + ", "+ valueColumnName + ") " + " values (?,?)";
	}
	
	public void increment() {
		accessCount++;
	}
	
	public long getAccessCount() {
		return accessCount;
	}
	
	public Serializable generate(final SessionImplementor session) {
		final SqlStatementLogger statementLogger = session.getFactory().getServiceRegistry()
				.getService( JdbcServices.class )
				.getSqlStatementLogger();
		final SessionEventListenerManager statsCollector = session.getEventListenerManager();

		return getOptimizer().generate(
				new AccessCallback() {
					@Override
					public IntegralDataTypeHolder getNextValue() {
						return session.getTransactionCoordinator().createIsolationDelegate().delegateWork(
								new AbstractReturningWork<IntegralDataTypeHolder>() {
									@Override
									public IntegralDataTypeHolder execute(Connection connection) throws SQLException {
										final IntegralDataTypeHolder value = IdentifierGeneratorHelper.getIntegralDataTypeHolder(Long.class);
										int rows;
										do {
											final PreparedStatement selectPS = prepareStatement( connection, buildSelectQuery(), statementLogger, statsCollector );

											try {
												LOG.debug(segmentValue);
												selectPS.setString( 1, segmentValue);
												final ResultSet selectRS = executeQuery( selectPS, statsCollector );
												boolean has=selectRS.next();
												if ( !has ) {
													value.initialize( initialValue );

													final PreparedStatement insertPS = prepareStatement( connection, buildInsertQuery(), statementLogger, statsCollector );
													try {
														insertPS.setString( 1, segmentValue );
														value.bind( insertPS, 2 );
														executeUpdate( insertPS, statsCollector );
													}
													finally {
														insertPS.close();
													}
												}
												else {
													value.initialize( selectRS, 1 );
												}
												selectRS.close();
											}
											catch ( SQLException e ) {
											    LOG.unableToReadOrInitHiValue(e);
												throw e;
											}
											finally {
												selectPS.close();
											}


											final PreparedStatement updatePS = prepareStatement( connection, buildUpdateQuery(), statementLogger, statsCollector );
											try {
												final IntegralDataTypeHolder updateValue = value.copy();
												if ( getOptimizer().applyIncrementSizeToSourceValues() ) {
													updateValue.add( incrementSize );
												}
												else {
													updateValue.increment();
												}
												updateValue.bind( updatePS, 1 );
												value.bind( updatePS, 2 );
												updatePS.setString( 3, segmentValue );
												rows = executeUpdate( updatePS, statsCollector );
											}
											catch ( SQLException e ) {
												LOG.unableToUpdateQueryHiValue(tableName, e);
												throw e;
											}
											finally {
												updatePS.close();
											}
										}
										while ( rows == 0 );
										increment();
										return value;
									}
								},
								true
						);
					}
					@Override
					public String getTenantIdentifier() {
						return session.getTenantIdentifier();
					}
				}
		);
	}

	private PreparedStatement prepareStatement(Connection connection,
			String sql, SqlStatementLogger statementLogger,
			SessionEventListenerManager statsCollector) throws SQLException {
		statementLogger.logStatement(sql, FormatStyle.BASIC.getFormatter());
		try {
			statsCollector.jdbcPrepareStatementStart();
			return connection.prepareStatement(sql);
		} finally {
			statsCollector.jdbcPrepareStatementEnd();
		}
	}

	private int executeUpdate(PreparedStatement ps,
			SessionEventListenerManager statsCollector) throws SQLException {
		try {
			statsCollector.jdbcExecuteStatementStart();
			return ps.executeUpdate();
		} finally {
			statsCollector.jdbcExecuteStatementEnd();
		}

	}

	private ResultSet executeQuery(PreparedStatement ps,
			SessionEventListenerManager statsCollector) throws SQLException {
		try {
			statsCollector.jdbcExecuteStatementStart();
			return ps.executeQuery();
		} finally {
			statsCollector.jdbcExecuteStatementEnd();
		}
	}

	public Optimizer getOptimizer() {
		return optimizer;
	}

	public String getTableName() {
		return tableName;
	}

	public String getSegmentColumnName() {
		return segmentColumnName;
	}

	public String getValueColumnName() {
		return valueColumnName;
	}
	public long getSegmentValueLength() {
		return 255;
	}
}
