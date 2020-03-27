package ac.project.Robal.models;

import java.util.List;

public class Order {

	private Long orderId;	
	private Long orderTotal;
	private List<String> orderItems;
	
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(Long orderTotal) {
		this.orderTotal = orderTotal;
	}
	public List<String> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<String> orderItems) {
		this.orderItems = orderItems;
	}
	
	
}
