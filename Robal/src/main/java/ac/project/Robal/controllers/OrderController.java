package ac.project.Robal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.services.OrderService;

public class OrderController {

	private OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/orders")
	public Order saveOrder(@RequestBody Order order, @RequestBody Customer customer) throws Exception {
		return orderService.saveOrder(order, customer);
	}
}
