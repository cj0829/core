package org.csr.core.persistence;

import java.io.Serializable;
import java.util.List;

import org.csr.core.Persistable;

public interface INode<N,PK extends Serializable> extends Persistable<PK>{
	
	public String getName();
	
	public void setName(String name);
	
	Integer getSize();
	
	void setSize(Integer size);

	Integer getIndex();
	
	List<N> getChild();
	
	void setIndex(Integer index);
	
	String toPath();
}
