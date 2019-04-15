package org.csr.core;

import java.io.Serializable;

public interface Persistable<ID extends Serializable> extends Serializable {
	
	ID getId();

	public void setId(ID id);

}
