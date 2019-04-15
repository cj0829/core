package org.csr.core.persistence.support;

import java.io.Serializable;

import javax.persistence.metamodel.SingularAttribute;

import org.csr.core.persistence.EntityInfo;

/**
 * 
 */
public interface JpaEntityInfo<T, ID extends Serializable> extends EntityInfo<T, ID> {

	SingularAttribute<? super T, ?> getIdAttribute();

}
