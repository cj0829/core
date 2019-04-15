package org.csr.core.persistence.query.hibernate;

import org.csr.core.persistence.query.DecorateQuery;
import org.hibernate.Query;


public class HibernateQuery implements DecorateQuery{
	final Query query;

	public HibernateQuery(Query query) {
		this.query = query;
	}

	@Override
	public void setParameter(String name, Object value) {
		this.query.setParameter(name, value);
	}
}
