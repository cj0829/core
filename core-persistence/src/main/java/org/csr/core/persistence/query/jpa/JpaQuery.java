package org.csr.core.persistence.query.jpa;

import javax.persistence.Query;

import org.csr.core.persistence.query.DecorateQuery;


public class JpaQuery implements DecorateQuery{
	
	final Query query;

	
	public JpaQuery(Query query) {
		this.query = query;
	}


	@Override
	public void setParameter(String name, Object value) {
		this.query.setParameter(name, value);
	}
}
