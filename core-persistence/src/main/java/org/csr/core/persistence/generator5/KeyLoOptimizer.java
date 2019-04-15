package org.csr.core.persistence.generator5;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.internal.CoreMessageLogger;
import org.jboss.logging.Logger;

public class KeyLoOptimizer implements Optimizer {
	
	protected final Class returnClass;
	protected final int incrementSize;
	
	private static final CoreMessageLogger log = Logger.getMessageLogger(CoreMessageLogger.class, GlobalSequence.class.getName());
	private IntegralDataTypeHolder lastSourceValue;
	private IntegralDataTypeHolder cacheMaxValue;
	private IntegralDataTypeHolder value;

	public KeyLoOptimizer(Class returnClass, int incrementSize) {
		
		this.returnClass = returnClass;
		this.incrementSize = incrementSize;
		if ( incrementSize < 1 ) {
			throw new HibernateException( "increment size cannot be less than 1" );
		}
		if ( log.isTraceEnabled() ) {
			log.tracev( "Creating hilo optimizer with [incrementSize={0}; returnClass={1}]", incrementSize, returnClass.getName() );
		}
	}

	@Override
	public synchronized Serializable generate(AccessCallback callback) {
		if (lastSourceValue == null) {
			lastSourceValue = callback.getNextValue();
			while (lastSourceValue.lt(1)) {
				lastSourceValue = callback.getNextValue();
			}
			cacheMaxValue = lastSourceValue.copy().add(incrementSize);
			value = lastSourceValue.copy();
		} else if (!cacheMaxValue.gt(value)) {
			lastSourceValue = callback.getNextValue();
			value = lastSourceValue.copy();
			cacheMaxValue = lastSourceValue.copy().add(incrementSize);
		}
		return value.makeValueThenIncrement();
	}

	@Override
	public IntegralDataTypeHolder getLastSourceValue() {
		return lastSourceValue;
	}

	@Override
	public boolean applyIncrementSizeToSourceValues() {
		return true;
	}

	@Override
	public final int getIncrementSize() {
		return incrementSize;
	}
}