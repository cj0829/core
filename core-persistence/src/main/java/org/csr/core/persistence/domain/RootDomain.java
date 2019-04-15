package org.csr.core.persistence.domain;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.csr.core.Persistable;

//JPA 基类的标识
@MappedSuperclass
public abstract class RootDomain<PK extends Serializable> implements Persistable<PK>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2276273021964066157L;
	
	
	@Transient
	public boolean isNew() {
		return null == getId();
	}

	public boolean equals(Object obj)
	  {
	    if (obj == null)
	      return false;
	    if (this == obj)
	      return true;
	    if (!Persistable.class.isAssignableFrom(obj.getClass()))
	      return false;
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		Persistable localBaseEntity = (Persistable<PK>) obj;
	    return getId() != null ? getId().equals(localBaseEntity.getId()) : false;
	  }

	  public int hashCode()
	  {
	    int i = 17;
	    i += (getId() == null ? 0 : getId().hashCode() * 31);
	    return i;
	  }
}
