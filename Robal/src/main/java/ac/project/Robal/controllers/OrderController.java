package ac.project.Robal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.models.Order;
import ac.project.Robal.services.OrderService;
import javassist.NotFoundException;

@RestController
public class OrderController {

	private OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/orders")
	public Order saveOrder(@RequestBody Order order) throws Exception {
		return orderService.saveOrder(order);
	}

	@GetMapping("/orders/{id}")
	public Order findOrder(@PathVariable Long id) {
		return orderService.findOrder(id);
	}

	@DeleteMapping("/orders/{id}")
	public void deleteOrder(@PathVariable Long id) throws NotFoundException {
		orderService.deleteOrder(id);
	}

	@PutMapping("/orders/{id}")
	public Order updateOrder(@RequestBody Order order) throws Exception {
		return orderService.saveOrder(order);
	}

}
