package org.csr.core.persistence.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.csr.core.Param;
import org.csr.core.exception.Exceptions;
import org.csr.core.page.Sort;
import org.csr.core.persistence.CacheableParam;
import org.csr.core.persistence.Finder;
import org.csr.core.persistence.QueryParam;
import org.csr.core.persistence.param.AndIsValue;
import org.csr.core.persistence.param.AndLikeParam;
import org.csr.core.persistence.param.AndNotIsValue;
import org.csr.core.persistence.param.AndParam;
import org.csr.core.persistence.param.OrIsValue;
import org.csr.core.persistence.param.OrLikeParam;
import org.csr.core.persistence.param.OrNotIsValue;
import org.csr.core.persistence.param.OrParam;
import org.csr.core.util.ObjUtil;

/**
 * ClassName:FinderImpl.java <br/>
 * System Name：  <br/>
 * Date: 2015年5月21日下午12:26:52 <br/>
 * 
 * @author caijin <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7
 * 
 *        功能描述： 查询对象,的实现 <br/>
 */
public class FinderImpl implements Finder {

	protected FinderImpl() {
		hqlBuilder = new StringBuilder();
		endBuilder = new StringBuilder("");
	}

	protected FinderImpl(String hql) {
		hqlBuilder = new StringBuilder(hql);
		endBuilder = new StringBuilder("");
	}

	public static Finder create(String hql) {
		return new FinderImpl(hql);
	}

	public boolean isAutoParam() {
		return this.autoParam;
	};

	public void setAutoParam(boolean autoParam) {
		this.autoParam = autoParam;
	};

	@Override
	public Finder append(Object hql) {
		hqlBuilder.append(hql);
		return this;
	}

	@Override
	public Finder append(Object hql, String param, Object value) {
		hqlBuilder.append(hql);
		setParam(param, value);
		return this;
	}

	@Override
	public Finder insertEnd(Object hql) {
		endBuilder.append(hql);
		return this;
	}

	public Finder setDetectAlias(String detectAlias) {
		this.detectAlias = detectAlias;
		return this;
	}

	@Override
	public String getDetectAlias() {
		return this.detectAlias;
	}

	@Override
	public Finder addWhereNotAliasValue(String field, String compare,
			String value) {
		addValue(this.hqlBuilder, Compare.AND, field, compare, value, false);
		return this;
	}

	@Override
	public Finder addWhereValue(String field, String compare, String value) {
		String hql = hqlBuilder.toString();
		if (ObjUtil.isBlank(detectAlias)) {
			detectAlias = QueryUtils.detectAlias(hql);
		}
		addValue(this.hqlBuilder, Compare.AND, field, compare, value, true);
		return this;
	}

	@Override
	public Finder orWhereValue(String field, String compare, String value) {
		String hql = hqlBuilder.toString();
		if (ObjUtil.isBlank(detectAlias)) {
			detectAlias = QueryUtils.detectAlias(hql);
		}
		addValue(this.hqlBuilder, Compare.OR, field, compare, value, true);
		return this;
	}

	@Override
	public String getHql() {
		StringBuilder newHql = new StringBuilder(hqlBuilder.toString());
		Sort newSort=null;
		if (ObjUtil.isBlank(detectAlias)) {
			detectAlias = QueryUtils.detectAlias(hqlBuilder.toString());
		}
		if (this.autoParam) {
			Iterator<String> it = autoParamMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<Param> autoParamList = autoParamMap.get(key);
				for (int i = 0; ObjUtil.isNotEmpty(autoParamList) && i < autoParamList.size(); i++) {
					Param param = autoParamList.get(i);
					if(param instanceof QueryParam){
						QueryParam queryParam = (QueryParam) param;
						if(ObjUtil.isNotEmpty(queryParam.getValue())){
							queryParam.setKeyPrefix(key+"_"+i);
							paramToHql(newHql, queryParam);
						}
					}
					if(param instanceof Sort){
						if(ObjUtil.isEmpty(newSort)){
							newSort=(Sort) param;
						}else{
							newSort=newSort.and((Sort)param);
						}
					}
				}
			}
		}
		newHql.append(endBuilder);
		if(ObjUtil.isNotEmpty(sort)){
			if(ObjUtil.isNotEmpty(newSort)){
				String sql=QueryUtils.applySorting(newHql.toString(), newSort.and(sort), detectAlias);
				return sql;
			}else{
				String sql=QueryUtils.applySorting(newHql.toString(), sort, detectAlias);
				return sql;
			}
		}else{
			if(ObjUtil.isNotEmpty(newSort)){
				String sql=QueryUtils.applySorting(newHql.toString(), newSort, detectAlias);
				return sql;
			}
		}
		//
		return newHql.toString();
	}

	@Override
	public String getRowCountHql() {
		return QueryUtils.createCountQuery(getHql().toString(),getDetectAlias());
	}

	@Override
	public boolean isCacheable() {
		return cacheable;
	}

	@Override
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
	@Override
	public Finder setSort(Sort sort) {
		this.sort=sort;
		return this;
	}
	@Override
	public Finder setParam(Object value) {
		getValues().add(value);
		return this;
	}

	@Override
	public Finder setParam(String param, Object value) {
		getParams().add(param);
		getValues().add(value);
		return this;
	}

	@Override
	public Finder setParams(Map<String, Object> paramMap) {
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			setParam(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public List<String> getParams() {
		if (params == null) {
			params = new ArrayList<String>();
		}
		return params;
	}

	public List<Object> getValues() {
		if (values == null) {
			values = new ArrayList<Object>();
		}
		return values;
	}

	public Finder setParamsToQuery(DecorateQuery query) {
		if (ObjUtil.isNotEmpty(this.getParams())) {
			for (int i = 0; i < this.getParams().size(); i++) {
				Object value = this.getValues().get(i);
				query.setParameter(this.getParams().get(i), value);
			}
		}
		// 自动提交
		if (this.autoParam) {
			Iterator<String> it = autoParamMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<Param> autoParamList = autoParamMap.get(key);
				for (int i = 0; ObjUtil.isNotEmpty(autoParamList) && i < autoParamList.size(); i++) {
					Param param = autoParamList.get(i);
					if (param instanceof QueryParam && !isNotAddParamType(param) ) {
						QueryParam queryParam=(QueryParam) param;
						if(ObjUtil.isNotEmpty(queryParam.getValue())){
							query.setParameter(queryParam.getPlaceholder(),queryParam.getValue());
						}
					}
				}
			}
		}
	
		return this;
	}

	public Finder putAutoParam(String name, List<Param> autoParam) {
		this.autoParamMap.put(name, autoParam);
		return this;
	}

	@Override
	public Finder autoPackagingParams(String name, List<Param> autoParamList) {
		if (ObjUtil.isEmpty(autoParamList)) {
			return this;
		}
		for (int i = 0; i < autoParamList.size(); i++) {
			Param param = autoParamList.get(i);
			if (ObjUtil.isNotBlank(param.getKey())) {
				if (param instanceof QueryParam) {
					QueryParam queryParam = (QueryParam) param;
					queryParam.setKeyPrefix(name+"_"+i);
					if( ObjUtil.isNotEmpty(queryParam.getValue())){
						paramToHql(this.hqlBuilder, queryParam);
						if(!isNotAddParamType(param)){
							setParam(queryParam.getPlaceholder(), queryParam.getValue());
						}
					}
				}
			}
			if(param instanceof Sort){
				if(ObjUtil.isEmpty(this.sort)){
					this.sort=(Sort) param;
				}else{
					this.sort=this.sort.and((Sort)param);
				}
			}
			if(param instanceof CacheableParam){
				CacheableParam cacheableParam= (CacheableParam) param;
				this.cacheable=cacheableParam.isCacheable();
			}
		}
		return this;
	}

	public void paramToHql(StringBuilder newBuilder, QueryParam param) {
		String operators="";
		if (param instanceof AndParam) {
			operators=Compare.AND;
		} else if (param instanceof OrParam) {
			operators=Compare.OR;
		} else {
			Exceptions.dao("", "您的sql语句没有or 或 and 连接，请您检查sql");
		}
		String hql = newBuilder.toString();
		if (ObjUtil.isBlank(detectAlias)) {
			detectAlias = QueryUtils.detectAlias(hql);
		}
		Matcher del = QueryUtils.DELETE_FROM.matcher(hql);
		
		Matcher where = null;
		if(del.find()){
			where = QueryUtils.DELETE_WHERE.matcher(hql);
		}else{
			where = QueryUtils.SELECT_WHERE.matcher(hql);
		}
		
		boolean isNotEmpty = ObjUtil.isNotEmpty(param.getChildParam());
		if (where.find()) {
			newBuilder.append(" " + operators + " ");
			if (isNotEmpty) {
				newBuilder.append("(");
			}
			newBuilder.append(detectAlias + ".");
			newBuilder.append(param.getKey() +" "+ param.compare() +" "+ param.hql());

		} else {
			newBuilder.append(" where 1=1 " + operators + " ");
			if (isNotEmpty) {
				newBuilder.append("(");
			}
			newBuilder.append(detectAlias + ".");
			newBuilder.append(param.getKey() +" "+ param.compare()+ " " + param.hql());
		}
		if (isNotEmpty) {
			addChildItemParam(newBuilder, param.getKey(), param.getChildParam());
			newBuilder.append(")");
		}
		
	}

	protected Finder addChildItemParam(StringBuilder newBuilder, String name,
			List<QueryParam> autoParam) {
		if (ObjUtil.isEmpty(autoParam)) {
			return this;
		}
		for (int i = 0; i < autoParam.size(); i++) {
			QueryParam param = autoParam.get(i);
			if (ObjUtil.isNotBlank(param.getKey()) && ObjUtil.isNotEmpty(param.getValue())) {
				paramToHql(this.hqlBuilder, (QueryParam) param);
			}
		}
		return this;
	}

	private Finder addValue(StringBuilder newBuilder, String operators,
			String field, String compare, String value, boolean isDetectAlias) {

		String hql = newBuilder.toString();
		if (ObjUtil.isBlank(detectAlias)) {
			detectAlias = QueryUtils.detectAlias(hql);
		}
		Matcher select = QueryUtils.SELECT_WHERE.matcher(hql);
		if (select.find()) {
			newBuilder.append(" " + operators + " ");
			if (isDetectAlias) {
				newBuilder.append(detectAlias + ".");
			}
			newBuilder.append(field + " " + compare + " " + value);

		} else {
			newBuilder.append(" where 1=1 " + operators + " ");
			if (isDetectAlias) {
				newBuilder.append(detectAlias + ".");
			}
			newBuilder.append(field + " " + compare + " " + value);
		}
		return this;
	}

	private boolean isNotAddParamType(Param param) {
		return param instanceof AndIsValue || param instanceof AndNotIsValue
				|| param instanceof AndLikeParam || param instanceof OrIsValue
				|| param instanceof OrNotIsValue
				|| param instanceof OrLikeParam;
	}
	
	private final StringBuilder hqlBuilder;
	private final StringBuilder endBuilder;
	private Sort sort;
	private List<String> params;
	private List<Object> values;

	private final Map<String, List<Param>> autoParamMap = new HashMap<String, List<Param>>();
	private boolean autoParam = true;
	private String detectAlias;

	private boolean cacheable = false;

	

}