package org.csr.core.persistence.generator5;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MappingException;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.engine.spi.SessionEventListenerManager;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.ExportableColumn;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.jdbc.AbstractReturningWork;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.jboss.logging.Logger;

public class CustomTableGenerator implements PersistentIdentifierGenerator, Configurable {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, CustomTableGenerator.class.getName());

	public static final String CONFIG_PREFER_SEGMENT_PER_ENTITY = "prefer_entity_table_as_segment_value";

	public static final String TABLE_PARAM = "table_name";
	public static final String DEF_TABLE = "pmt_tb_generator";

	public static final String VALUE_COLUMN_PARAM = "value_column_name";
	public static final String DEF_VALUE_COLUMN = "sequence_next_hi_value";

	public static final String SEGMENT_COLUMN_PARAM = "segment_column_name";
	public static final String DEF_SEGMENT_COLUMN = "sequence_name";

	public static final String SEGMENT_VALUE_PARAM = "segment_value";
	public static final String DEF_SEGMENT_VALUE = "default_custom";

	public static final String SEGMENT_LENGTH_PARAM = "segment_value_length";
	public static final int DEF_SEGMENT_LENGTH = 255;

	public static final String INITIAL_PARAM = "initial_value";
	public static final int DEFAULT_INITIAL_VALUE = 1;

	public static final String INCREMENT_PARAM = "increment_size";
	public static final int DEFAULT_INCREMENT_SIZE = 100;

	public static final String OPT_PARAM = "optimizer";


	private Type identifierType;

	private QualifiedName qualifiedTableName;
	private String renderedTableName;
	
	private String segmentColumnName;
	private String segmentValue;
	private int segmentValueLength;

	private String valueColumnName;
	private int initialValue;
	private int incrementSize;

	private String selectQuery;
	private String insertQuery;
	private String updateQuery;

	private Optimizer optimizer;
	private long accessCount = 0;

	@Override
	public Object generatorKey() {
		return qualifiedTableName.render();
	}
	/**
	 * Type mapping for the identifier.
	 *
	 * @return The identifier type mapping.
	 */
	public final Type getIdentifierType() {
		return identifierType;
	}
	/**
	 * The name of the table in which we store this generator's persistent state.
	 *
	 * @return The table name.
	 */
	public final String getTableName() {
		return qualifiedTableName.render();
	}
	/**
	 * The name of the column in which we store the segment to which each row
	 * belongs.  The value here acts as PK.
	 *
	 * @return The segment column name
	 */
	public final String getSegmentColumnName() {
		return segmentColumnName;
	}

	/**
	 * The value in {@link #getSegmentColumnName segment column} which
	 * corresponding to this generator instance.  In other words this value
	 * indicates the row in which this generator instance will store values.
	 *
	 * @return The segment value for this generator instance.
	 */
	public final String getSegmentValue() {
		return segmentValue;
	}

	/**
	 * The size of the {@link #getSegmentColumnName segment column} in the
	 * underlying table.
	 * <p/>
	 * <b>NOTE</b> : should really have been called 'segmentColumnLength' or
	 * even better 'segmentColumnSize'
	 *
	 * @return the column size.
	 */
	public final int getSegmentValueLength() {
		return segmentValueLength;
	}

	/**
	 * The name of the column in which we store our persistent generator value.
	 *
	 * @return The name of the value column.
	 */
	public final String getValueColumnName() {
		return valueColumnName;
	}

	/**
	 * The initial value to use when we find no previous state in the
	 * generator table corresponding to our sequence.
	 *
	 * @return The initial value to use.
	 */
	public final int getInitialValue() {
		return initialValue;
	}

	/**
	 * The amount of increment to use.  The exact implications of this
	 * depends on the {@link #getOptimizer() optimizer} being used.
	 *
	 * @return The increment amount.
	 */
	public final int getIncrementSize() {
		return incrementSize;
	}

	/**
	 * The optimizer being used by this generator.
	 *
	 * @return Out optimizer.
	 */
	public final Optimizer getOptimizer() {
		return optimizer;
	}

	/**
	 * Getter for property 'tableAccessCount'.  Only really useful for unit test
	 * assertions.
	 *
	 * @return Value for property 'tableAccessCount'.
	 */
	public final long getTableAccessCount() {
		return accessCount;
	}


	@Override
	public void configure(Type type, Properties params,ServiceRegistry serviceRegistry) throws MappingException {
		identifierType = type;
		final JdbcEnvironment jdbcEnvironment = serviceRegistry.getService( JdbcEnvironment.class );

		qualifiedTableName = determineGeneratorTableName( params, jdbcEnvironment );
		segmentColumnName = determineSegmentColumnName( params, jdbcEnvironment );
		valueColumnName = determineValueColumnName( params, jdbcEnvironment );

		segmentValue = determineSegmentValue( params );

		segmentValueLength = determineSegmentColumnSize( params );
		initialValue = determineInitialValue( params );
		incrementSize = determineIncrementSize( params );
		
		optimizer = new KeyLoOptimizer(identifierType.getReturnedClass(), incrementSize);
	}
	/**
	 * Determine the table name to use for the generator values.
	 * <p/>
	 * Called during {@link #configure configuration}.
	 *
	 * @see #getTableName()
	 * @param params The params supplied in the generator config (plus some standard useful extras).
	 * @param dialect The dialect in effect
	 * @return The table name to use.
	 */
	protected QualifiedName determineGeneratorTableName(Properties params, JdbcEnvironment jdbcEnvironment) {
		final String tableName = ConfigurationHelper.getString( TABLE_PARAM, params, DEF_TABLE );

		if ( tableName.contains( "." ) ) {
			return QualifiedNameParser.INSTANCE.parse( tableName );
		}
		else {
			// todo : need to incorporate implicit catalog and schema names
			final Identifier catalog = jdbcEnvironment.getIdentifierHelper().toIdentifier(
					ConfigurationHelper.getString( CATALOG, params )
			);
			final Identifier schema = jdbcEnvironment.getIdentifierHelper().toIdentifier(
					ConfigurationHelper.getString( SCHEMA, params )
			);
			return new QualifiedNameParser.NameParts(
					catalog,
					schema,
					jdbcEnvironment.getIdentifierHelper().toIdentifier( tableName )
			);
		}
	}

	/**
	 * Determine the name of the column used to indicate the segment for each
	 * row.  This column acts as the primary key.
	 * <p/>
	 * Called during {@link #configure configuration}.
	 *
	 * @see #getSegmentColumnName()
	 * @param params The params supplied in the generator config (plus some standard useful extras).
	 * @param jdbcEnvironment The JDBC environment
	 * @return The name of the segment column
	 */
	protected String determineSegmentColumnName(Properties params, JdbcEnvironment jdbcEnvironment) {
		final String name = ConfigurationHelper.getString( SEGMENT_COLUMN_PARAM, params, DEF_SEGMENT_COLUMN );
		return jdbcEnvironment.getIdentifierHelper().toIdentifier( name ).render( jdbcEnvironment.getDialect() );
	}

	/**
	 * Determine the name of the column in which we will store the generator persistent value.
	 * <p/>
	 * Called during {@link #configure configuration}.
	 *
	 * @see #getValueColumnName()
	 * @param params The params supplied in the generator config (plus some standard useful extras).
	 * @param jdbcEnvironment The JDBC environment
	 * @return The name of the value column
	 */
	protected String determineValueColumnName(Properties params, JdbcEnvironment jdbcEnvironment) {
		final String name = ConfigurationHelper.getString( VALUE_COLUMN_PARAM, params, DEF_VALUE_COLUMN );
		return jdbcEnvironment.getIdentifierHelper().toIdentifier( name ).render( jdbcEnvironment.getDialect() );
	}

	/**
	 * Determine the segment value corresponding to this generator instance.
	 * <p/>
	 * Called during {@link #configure configuration}.
	 *
	 * @see #getSegmentValue()
	 * @param params The params supplied in the generator config (plus some standard useful extras).
	 * @return The name of the value column
	 */
	protected String determineSegmentValue(Properties params) {
		String segmentValue = params.getProperty( SEGMENT_VALUE_PARAM );
		if ( StringHelper.isEmpty( segmentValue ) ) {
			segmentValue = determineDefaultSegmentValue( params );
		}
		return segmentValue;
	}
	/**
	 * Used in the cases where {@link #determineSegmentValue} is unable to
	 * determine the value to use.
	 *
	 * @param params The params supplied in the generator config (plus some standard useful extras).
	 * @return The default segment value to use.
	 */
	protected String determineDefaultSegmentValue(Properties params) {
		final boolean preferSegmentPerEntity = ConfigurationHelper.getBoolean( CONFIG_PREFER_SEGMENT_PER_ENTITY, params, false );
		final String defaultToUse = preferSegmentPerEntity ? params.getProperty( TABLE ) : DEF_SEGMENT_VALUE;
		LOG.usingDefaultIdGeneratorSegmentValue( qualifiedTableName.render(), segmentColumnName, defaultToUse );
		return defaultToUse;
	}

	/**
	 * Determine the size of the {@link #getSegmentColumnName segment column}
	 * <p/>
	 * Called during {@link #configure configuration}.
	 *
	 * @see #getSegmentValueLength()
	 * @param params The params supplied in the generator config (plus some standard useful extras).
	 * @return The size of the segment column
	 */
	protected int determineSegmentColumnSize(Properties params) {
		return ConfigurationHelper.getInt( SEGMENT_LENGTH_PARAM, params, DEF_SEGMENT_LENGTH );
	}

	protected int determineInitialValue(Properties params) {
		return ConfigurationHelper.getInt( INITIAL_PARAM, params, DEFAULT_INITIAL_VALUE );
	}

	protected int determineIncrementSize(Properties params) {
		return ConfigurationHelper.getInt( INCREMENT_PARAM, params, DEFAULT_INCREMENT_SIZE );
	}

	protected String buildSelectQuery(Dialect dialect) {
		final String alias = "tbl";
		final String query = "select " + StringHelper.qualify( alias, valueColumnName ) +
				" from " + renderedTableName + ' ' + alias +
				" where " + StringHelper.qualify( alias, segmentColumnName ) + "=?";
		final LockOptions lockOptions = new LockOptions( LockMode.PESSIMISTIC_WRITE );
		lockOptions.setAliasSpecificLockMode( alias, LockMode.PESSIMISTIC_WRITE );
		final Map<String,String[]> updateTargetColumnsMap = Collections.singletonMap( alias, new String[] { valueColumnName } );
		return dialect.applyLocksToSql( query, lockOptions, updateTargetColumnsMap );
	}


	protected String buildUpdateQuery() {
		return "update " + renderedTableName +
				" set " + valueColumnName + "=? " +
				" where " + valueColumnName + "=? and " + segmentColumnName + "=?";
	}
	
	protected String buildInsertQuery() {
		return "insert into " + renderedTableName + " (" + segmentColumnName + ", " + valueColumnName + ") " + " values (?,?)";
	}

	private IntegralDataTypeHolder makeValue() {
		return IdentifierGeneratorHelper.getIntegralDataTypeHolder( identifierType.getReturnedClass() );
	}

	@Override
	public Serializable generate(final SessionImplementor session, final Object obj) {
		final SqlStatementLogger statementLogger = session.getFactory().getServiceRegistry()
				.getService( JdbcServices.class )
				.getSqlStatementLogger();
		final SessionEventListenerManager statsCollector = session.getEventListenerManager();

		return optimizer.generate(
				new AccessCallback() {
					@Override
					public IntegralDataTypeHolder getNextValue() {
						return session.getTransactionCoordinator().createIsolationDelegate().delegateWork(
								new AbstractReturningWork<IntegralDataTypeHolder>() {
									@Override
									public IntegralDataTypeHolder execute(Connection connection) throws SQLException {
										final IntegralDataTypeHolder value = makeValue();
										int rows;
										do {
											final PreparedStatement selectPS = prepareStatement( connection, selectQuery, statementLogger, statsCollector );

											try {
												selectPS.setString( 1, segmentValue );
												final ResultSet selectRS = executeQuery( selectPS, statsCollector );
												if ( !selectRS.next() ) {
													value.initialize( initialValue );

													final PreparedStatement insertPS = prepareStatement( connection, insertQuery, statementLogger, statsCollector );
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


											final PreparedStatement updatePS = prepareStatement( connection, updateQuery, statementLogger, statsCollector );
											try {
												final IntegralDataTypeHolder updateValue = value.copy();
												if ( optimizer.applyIncrementSizeToSourceValues() ) {
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
												LOG.unableToUpdateQueryHiValue(renderedTableName, e);
												throw e;
											}
											finally {
												updatePS.close();
											}
										}
										while ( rows == 0 );

										accessCount++;

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

	private PreparedStatement prepareStatement(
			Connection connection,
			String sql,
			SqlStatementLogger statementLogger,
			SessionEventListenerManager statsCollector) throws SQLException {
		statementLogger.logStatement( sql, FormatStyle.BASIC.getFormatter() );
		try {
			statsCollector.jdbcPrepareStatementStart();
			return connection.prepareStatement( sql );
		}
		finally {
			statsCollector.jdbcPrepareStatementEnd();
		}
	}

	private int executeUpdate(PreparedStatement ps, SessionEventListenerManager statsCollector) throws SQLException {
		try {
			statsCollector.jdbcExecuteStatementStart();
			return ps.executeUpdate();
		}
		finally {
			statsCollector.jdbcExecuteStatementEnd();
		}

	}

	private ResultSet executeQuery(PreparedStatement ps, SessionEventListenerManager statsCollector) throws SQLException {
		try {
			statsCollector.jdbcExecuteStatementStart();
			return ps.executeQuery();
		}
		finally {
			statsCollector.jdbcExecuteStatementEnd();
		}
	}


	@Override
	public String[] sqlCreateStrings(Dialect dialect) throws HibernateException {
		return new String[] {
				dialect.getCreateTableString() + ' ' + renderedTableName + " ( "
						+ segmentColumnName + ' ' + dialect.getTypeName( Types.VARCHAR, segmentValueLength, 0, 0 ) + " not null "
						+ ", " + valueColumnName + ' ' + dialect.getTypeName( Types.BIGINT )
						+ ", primary key ( " + segmentColumnName + " ) )" + dialect.getTableTypeString()
		};
	}

	@Override
	public String[] sqlDropStrings(Dialect dialect) throws HibernateException {
		return new String[] { dialect.getDropTableString( renderedTableName ) };
	}
	
	@Override
	public void registerExportables(Database database) {
		final Dialect dialect = database.getJdbcEnvironment().getDialect();

		final Namespace namespace = database.locateNamespace(
				qualifiedTableName.getCatalogName(),
				qualifiedTableName.getSchemaName()
		);

		Table table = namespace.locateTable( qualifiedTableName.getObjectName() );
		if ( table == null ) {
			table = namespace.createTable( qualifiedTableName.getObjectName(), false );

			// todo : note sure the best solution here.  do we add the columns if missing?  other?
			final Column segmentColumn = new ExportableColumn(
					database,
					table,
					segmentColumnName,
					StringType.INSTANCE,
					dialect.getTypeName( Types.VARCHAR, segmentValueLength, 0, 0 )
			);
			segmentColumn.setNullable( false );
			table.addColumn( segmentColumn );

			// lol
			table.setPrimaryKey( new PrimaryKey( table ) );
			table.getPrimaryKey().addColumn( segmentColumn );

			final Column valueColumn = new ExportableColumn(
					database,
					table,
					valueColumnName,
					LongType.INSTANCE
			);
			table.addColumn( valueColumn );
		}

		// allow physical naming strategies a chance to kick in
		this.renderedTableName = database.getJdbcEnvironment().getQualifiedObjectNameFormatter().format(
				table.getQualifiedTableName(),
				dialect
		);

		this.selectQuery = buildSelectQuery( dialect );
		this.updateQuery = buildUpdateQuery();
		this.insertQuery = buildInsertQuery();

	}
}
