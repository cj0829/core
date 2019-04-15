package org.csr.core.persistence.query;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.csr.core.page.Order;
import org.csr.core.page.Sort;
import org.csr.core.persistence.Finder;
import org.csr.core.util.ObjUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Simple utility class to create JPA queries.
 * 
 */
public abstract class QueryUtils {

	public static final String ROW_COUNT = "count(*)";
	public static final String SELECT = "select";
	public static final String FROM = "from";
	public static final String WHERE = "where";
	public static final String EQUAL = "1=1";
	
	public static final String DISTINCT = "distinct";
	public static final String BLANK = " ";
	public static final Pattern ORDER_BY = Pattern.compile("(order)",Pattern.CASE_INSENSITIVE);
	public static final Pattern SELECT_FROM = Pattern.compile("(select)?(?<=from)", Pattern.CASE_INSENSITIVE);
	public static final Pattern SELECT_WHERE = Pattern.compile("(select).*(where)", Pattern.CASE_INSENSITIVE);
	public static final Pattern DELETE_FROM = Pattern.compile("(delete).*(from)", Pattern.CASE_INSENSITIVE);
	public static final Pattern DELETE_WHERE = Pattern.compile("(delete).*(where)", Pattern.CASE_INSENSITIVE);
	
	public static final String DEFAULT_ALIAS = "x";
	public static final String COUNT_QUERY_STRING = "select count(x.id) from %s x";
	public static final String EXISTS_QUERY_STRING = "select count(%s) from %s x where x.%s = :id";

	public static final String DELETE_ALL_QUERY_STRING = "delete from %s x";
	public static final String READ_ALL_QUERY = "select x from %s x";
	public static final String READ_FIELDNAME_QUERY = "select x.%s from %s x";
	public static final String READ_FIELD_ID_NAME_QUERY = "select x.id,x.%s from %s x";
	
	public static final String READ_DISTINCT_FIELDNAME_QUERY = "select distinct x.%s from %s x";
	
	private static final String COUNT_REPLACEMENT = "select count($3$5) $4$5$6";

	private static final Pattern ALIAS_MATCH;
	private static final Pattern COUNT_MATCH;

	private static final String IDENTIFIER = "[\\p{Alnum}._$]+";
	private static final String IDENTIFIER_GROUP = String.format("(%s)", IDENTIFIER);

	private static final String LEFT_JOIN = "left (outer )?join " + IDENTIFIER + " (as )?" + IDENTIFIER_GROUP;
	private static final Pattern LEFT_JOIN_PATTERN = Pattern.compile(LEFT_JOIN, Pattern.CASE_INSENSITIVE);

	static {

		StringBuilder builder = new StringBuilder();
		builder.append("(?<=from)"); // from as starting delimiter
		builder.append("(?: )+"); // at least one space separating
		builder.append(IDENTIFIER_GROUP); // Entity name, can be qualified (any
		builder.append("(?: as)*"); // exclude possible "as" keyword
		builder.append("(?: )+"); // at least one space separating
		builder.append("(\\w*)"); // the actual alias

		ALIAS_MATCH = compile(builder.toString(), CASE_INSENSITIVE);

		builder = new StringBuilder();
		builder.append("(select\\s+((distinct )?.+?)\\s+)?(from\\s+");
		builder.append(IDENTIFIER);
		builder.append("(?:\\s+as)?\\s+)");
		builder.append(IDENTIFIER_GROUP);
		builder.append("(.*)");

		COUNT_MATCH = compile(builder.toString(), CASE_INSENSITIVE);
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private QueryUtils() {

	}

	/**
	 * Returns the query string for the given class name.
	 * 
	 * @param template
	 * @param entityName
	 * @return
	 */
	public static String getQueryString(String template, Object... entityName) {
		Assert.notNull(entityName, "Entity name must not be null or empty!");
		return String.format(template, entityName);
	}

	
	/**
	 * Adds {@literal order by} clause to the JPQL query. Uses the {@link #DEFAULT_ALIAS} to bind the sorting property to.
	 * 
	 * @param query
	 * @param alias
	 * @param sort
	 * @return
	 */
	public static String applySorting(String query, Sort sort) {
		return applySorting(query, sort, DEFAULT_ALIAS);
	}

	/**
	 * Adds {@literal order by} clause to the JPQL query.
	 * 
	 * @param query
	 * @param sort
	 * @param alias
	 * @return
	 */
	public static String applySorting(String query, Sort sort, String alias) {
		Assert.hasText(query);
		if (null == sort || !sort.iterator().hasNext()) {
			return query;
		}
		StringBuilder builder = new StringBuilder(query);
		if (!query.contains("order by")) {
			builder.append(" order by ");
		} else {
			builder.append(", ");
		}
		Set<String> aliases = getOuterJoinAliases(query);
		for (Order order : sort) {
			builder.append(getOrderClause(aliases, alias, order));
		}
		builder.delete(builder.length() - 2, builder.length());
		return builder.toString();
	}
	
	/**
	 * Adds {@literal order by} clause to the JPQL query.
	 * 
	 * @param query
	 * @param sort
	 * @param alias
	 * @return
	 */
	public static String sortPropertys(Sort sort, String alias) {
		StringBuilder builder = new StringBuilder("");
		boolean qualifyReference = ObjUtil.isBlank(alias)?false:true;
		for (Order order : sort) {
			builder.append( String.format("%s %s, ", qualifyReference ? alias + "." : "", order.getProperty()));
		}
		builder.delete(builder.length() - 2, builder.length());
		return builder.toString();
	}
	
	
	/**
	 * Adds {@literal order by} clause to the JPQL query.
	 * 
	 * @param query
	 * @param sort
	 * @param alias
	 * @return
	 */
	public static String sortPropertysOrder(Sort sort, String alias) {
		StringBuilder builder = new StringBuilder("");
		boolean qualifyReference = ObjUtil.isBlank(alias)?false:true;
		for (Order order : sort) {
			builder.append(String.format("%s%s %s, ", qualifyReference ? alias + "." : "", order.getProperty(), toJpaDirection(order)));
		}
		builder.delete(builder.length() - 2, builder.length());
		return builder.toString();
	}
	
	
	private static String getOrderClause(Set<String> joinAliases, String alias, Order order) {
		String property = order.getProperty();
		boolean qualifyReference = true;
		for (String joinAlias : joinAliases) {
			if (property.startsWith(joinAlias)) {
				qualifyReference = false;
				break;
			}
		}
		return String.format("%s%s %s, ", qualifyReference ? alias + "." : "", property, toJpaDirection(order));
	}

	static Set<String> getOuterJoinAliases(String query) {
		Set<String> result = new HashSet<String>();
		Matcher matcher = LEFT_JOIN_PATTERN.matcher(query);
		while (matcher.find()) {
			String alias = matcher.group(3);
			if (StringUtils.hasText(alias)) {
				result.add(alias);
			}
		}
		return result;
	}

	private static String toJpaDirection(Order order) {
		return order.getDirection().name().toLowerCase(Locale.US);
	}

	public static String detectAlias(String query) {
		Matcher matcher = ALIAS_MATCH.matcher(query);
		return matcher.find() ? matcher.group(2) : null;
	}

	public static <T> Query applyAndBind(String queryString, Iterable<T> entities, EntityManager entityManager) {

		Assert.notNull(queryString);
		Assert.notNull(entities);
		Assert.notNull(entityManager);

		Iterator<T> iterator = entities.iterator();

		if (!iterator.hasNext()) {
			return entityManager.createQuery(queryString);
		}

		String alias = detectAlias(queryString);
		StringBuilder builder = new StringBuilder(queryString);
		builder.append(" where");

		int i = 0;

		while (iterator.hasNext()) {

			iterator.next();

			builder.append(String.format(" %s = ?%d", alias, ++i));

			if (iterator.hasNext()) {
				builder.append(" or");
			}
		}

		Query query = entityManager.createQuery(builder.toString());

		iterator = entities.iterator();
		i = 0;

		while (iterator.hasNext()) {
			query.setParameter(++i, iterator.next());
		}

		return query;
	}

	public static String createCountQuery(String originalQuery,String fileName) {
		Assert.hasText(originalQuery);
		Matcher orderMatcher = ORDER_BY.matcher(originalQuery);
		if (orderMatcher.find()) {
			StringBuffer buf = new StringBuffer();
			orderMatcher.appendReplacement(buf, "");
			originalQuery = buf.toString();
		}
		StringBuffer hql = new StringBuffer();
		StringBuffer _countHql = new StringBuffer();
		int endIndex=resolveJPDL(originalQuery,hql);
		if(endIndex>0){
			originalQuery=originalQuery.substring(0, endIndex);
		}
		_countHql.append(SELECT);
		_countHql.append(BLANK);
		if (originalQuery.toLowerCase().indexOf(DISTINCT) >= 0) {
			_countHql.append(DISTINCT);
			_countHql.append(BLANK);
		} 
		_countHql.append(ROW_COUNT);
//		_countHql.append("count(").append(fileName).append(".id)");
		_countHql.append(BLANK);
		_countHql.append(FROM);
		hql.insert(0, _countHql.toString());
		return hql.toString();
	}
	
	
	
	
	
	private static int resolveJPDL(String originalQuery,StringBuffer hql){
		int begin=0;
		int end=originalQuery.toLowerCase().indexOf(FROM);
		if(end>0){
			end+=FROM.length();
		}
		String subString=originalQuery.substring(begin,end);
		int isSelect=isSelect(subString);
		while (isSelect!=-1) {
			begin=end;
			end=originalQuery.indexOf(FROM, begin);
			if(end>0){
				end+=FROM.length();
			}
			isSelect=isSelect(originalQuery, begin, end);
		}
		hql.append(originalQuery.substring(end));
		
		return end;
	}
	
	/**
	 * isSelect: 描述方法的作用 <br/>
	 * @author caijin
	 * @param subString
	 * @return
	 * @since JDK 1.7
	 */
	private static int isSelect(String subString){
		int dselect=subString.toLowerCase().indexOf(SELECT);
		if(dselect==0){
			dselect=subString.toLowerCase().indexOf(SELECT, SELECT.length());
		}
		return dselect;
	}
	
	/**
	 * isSelect: 描述方法的作用 <br/>
	 * @author caijin
	 * @param subString
	 * @return
	 * @since JDK 1.7
	 */
	private static int isSelect(String subString,int begin,int end){
		String s=subString.substring(begin, end);
		return isSelect(s);
	}
	
	/**
	 * Creates a count projected query from the given orginal query.
	 * 
	 * @param originalQuery must not be {@literal null} or empty
	 * @return
	 */
	public static String createCountQueryFor(String originalQuery) {

		Assert.hasText(originalQuery);

		Matcher matcher = COUNT_MATCH.matcher(originalQuery);
		return matcher.replaceFirst(COUNT_REPLACEMENT);
	}

	/**
	 * Returns whether the given {@link Query} contains named parameters.
	 * 
	 * @param query
	 * @return
	 */
	public static boolean hasNamedParameter(Query query) {

		for (Parameter<?> parameter : query.getParameters()) {
			if (parameter.getName() != null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Executes a count query and transparently sums up all values returned.
	 * 
	 * @param query must not be {@literal null}.
	 * @return
	 */
	public static Long executeCountQuery(TypedQuery<Long> query) {

		Assert.notNull(query);

		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}
	
	class Dt{
		public Dt(int b){
			this.bigend=b;
//			this.end=e;
		}
		
		String context;
		
		String form;
		int bigend;
		int end;
		
	}
	public static void main(String[] args) {
		 Stack<Dt> sk = new Stack<Dt>();  
		Finder find = FinderImpl.create("select u from User u where u.userType=3 and u.id not in " +
				"(select distinct ur.userId from UserRole ur where ur.roleId = " +
				"(select from UserRole ur where ur.roleId = '(" +
				"select from UserRole ur where ur.roleId)')) " +
				"and ro=(select distinct ur.userId from UserRole ur where ur.roleId)");
		
		String originalQuery=find.getHql();
		int begin=0;
		int isSelect=isSelect(originalQuery);
		
//		Matcher isEmpty = SELECT_WHERE.matcher(find.getOriginalHql());
//		
//		System.out.println("=1"+isEmpty.find());
//		System.out.println("=1"+isEmpty.group());
		
		
//		Matcher matcher = SELECT_FROM.matcher(find.getOriginalHql());
//		System.out.println(matcher.find());
//		System.out.println("======2"+matcher.group());
//		
//		System.out.println(QueryUtils.applySorting("select * from TAble d", new Sort("name"),"d"));
//		
//		System.out.println(QueryUtils.createCountQueryFor("select sadf,asdf,from TAble d where sdf=1"));
//		System.out.println(QueryUtils.createCountQueryFor("select  sadf,asdf,from TAble d left join ths1 f on d.df=f.df"));
//		System.out.println(QueryUtils.detectAlias("select * from TAble d, tsr f where d.id=f.id"));
//		System.out.println(QueryUtils.getQueryString("select * from %s d", "tg"));
//		Finder finder=FinderImpl.create(QueryUtils.getQueryString(QueryUtils.READ_ALL_QUERY,"tablename"));
//		finder.append(" where 1=1")
//		;
//		System.out.println(QueryUtils.IDENTIFIER_GROUP);
//		Set<String> sets =QueryUtils.getOuterJoinAliases("select * from TAble d");
//		System.out.println(sets);
//		System.out.println(finder.addWhereValue("123",Compare.IN, "123").getOriginalHql());
	}
}
