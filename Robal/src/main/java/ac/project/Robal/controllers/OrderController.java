package ac.project.Robal.controllers;

import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.services.AccountService;
import ac.project.Robal.services.OrderService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

	private OrderService orderService;
	private AccountService accountService;

	@Autowired
	public OrderController(OrderService orderService,
						   AccountService accountService) {
		this.orderService = orderService;
		this.accountService = accountService;
	}

	@PostMapping("/orders")
	public Order saveOrder(@RequestBody List<OrderProduct> orderProducts) throws Exception {
		//TODO once principal is implemented, get the customer from that id.
		Customer customer = accountService.findCustomer(1L);
		return orderService.saveOrder(customer, orderProducts);
	}

	@GetMapping("/orders/{id}")
	public Order findOrder(@PathVariable Long id) {
		return orderService.findOrder(id);
	}

	@DeleteMapping("/orders/{id}")
	public void deleteOrder(@PathVariable Long id) throws NotFoundException {
		orderService.deleteOrder(id);
	}

/*	@PutMapping("/orders/{id}")
	public Order updateOrder(@RequestBody Order order) throws Exception {
		return orderService.saveOrder(order);
	}*/

}
