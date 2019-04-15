package org.csr.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {

	private static final long serialVersionUID = 1522511010900108987L;

	private final Direction direction;
	private final String property;

	public Order(String property) {
		this(Sort.DEFAULT_DIRECTION, property);
	}
	
	public Order(Direction direction, String property) {
//		if (!StringUtils.hasText(property)) {
//			throw new IllegalArgumentException("Property must not null or empty!");
//		}
		this.direction = direction == null ? Sort.DEFAULT_DIRECTION : direction;
		this.property = property;
	}

	@Deprecated
	public static List<Order> create(Direction direction,Iterable<String> properties) {
		List<Order> orders = new ArrayList<Order>();
		for (String property : properties) {
			orders.add(new Order(direction, property));
		}
		return orders;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getProperty() {
		return property;
	}

	public boolean isAscending() {
		return this.direction.equals(Direction.ASC);
	}

	public Order with(Direction order) {
		return new Order(order, this.property);
	}

	public Sort withProperties(String... properties) {
		return new Sort(this.direction, properties);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + direction.hashCode();
		result = 31 * result + property.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Order)) {
			return false;
		}
		Order that = (Order) obj;
		return this.direction.equals(that.direction) && this.property.equals(that.property);
	}

	@Override
	public String toString() {
		return String.format("%s: %s", property, direction);
	}
}