package org.csr.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.csr.core.Param;
import org.csr.core.util.ObjUtil;
import org.springframework.util.StringUtils;

public class Sort implements Iterable<Order>, Serializable, Param {

	private static final long serialVersionUID = 5737186511678863905L;
	public static final Direction DEFAULT_DIRECTION = Direction.ASC;

	private final List<Order> orders;

	public Sort(Order... orders) {
		this(ObjUtil.toList(orders));
	}

	public Sort(List<Order> orders) {
//		if (null == orders || orders.isEmpty()) {
//			throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
//		}
		this.orders = orders;
	}

	public Sort(String... properties) {
		this(DEFAULT_DIRECTION, properties);
	}

	public Sort(Direction direction, String... properties) {
		this(direction, properties == null ? new ArrayList<String>() : Arrays.asList(properties));
	}

	public Sort(Direction direction, List<String> properties) {
		if (properties == null || properties.isEmpty()) {
			throw new IllegalArgumentException("You have to provide at least one property to sort by!");
		}
		this.orders = new ArrayList<Order>(properties.size());
		for (String property : properties) {
			this.orders.add(new Order(direction, property));
		}
	}

	public Sort and(Sort sort) {
		if (sort == null) {
			return this;
		}
		ArrayList<Order> these = new ArrayList<Order>(this.orders);
		for (Order order : sort) {
			these.add(order);
		}
		return new Sort(these);
	}

	public Order getOrderFor(String property) {
		for (Order order : this) {
			if (order.getProperty().equals(property)) {
				return order;
			}
		}
		return null;
	}

	public Iterator<Order> iterator() {
		return this.orders.iterator();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Sort)) {
			return false;
		}
		Sort that = (Sort) obj;
		return this.orders.equals(that.orders);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + orders.hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		return StringUtils.collectionToCommaDelimitedString(orders);
	}

	@Override
	public String getKey() {
		return toString();
	}

}
