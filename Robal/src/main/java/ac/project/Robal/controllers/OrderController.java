package ac.project.Robal.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.enums.Role;
import ac.project.Robal.models.Account;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.services.AccountService;
import ac.project.Robal.services.OrderService;
import ac.project.Robal.utils.AccountUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class OrderController {

	Logger logger = LoggerFactory.getLogger(OrderController.class);

	private OrderService orderService;
	private AccountService accountService;

	@Autowired
	public OrderController(OrderService orderService,
			AccountService accountService) {
		this.orderService = orderService;
		this.accountService = accountService;
	}

	@ApiOperation(value = "Find an Order", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Order Found"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@GetMapping("/orders/{id}")
	public Order findOrder(@PathVariable Long id) {
		return orderService.findOrder(id);
	}

	@ApiOperation(value = "List all Orders", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Orders found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/orders/")
	public List<Order> listOrders() {
		return orderService.findOrders();
	}

	@ApiOperation(value = "List all Order Products", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Order Products found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/order-products/")
	public List<OrderProduct> listOrderProducts() {
		return orderService.findOrderProducts();
	}

	@ApiOperation(value = "Save an Order", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully saved Order."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/orders")
	public Order saveOrder(Principal principal, @RequestBody List<OrderProduct> orderProducts) throws Exception {
		logger.info("***saveOrder method accessed by " + principal.getName() + "***");

		Customer customer = null;
		customer = accountService.findCustomerByEmail(principal.getName());

		return orderService.saveOrder(customer, orderProducts);

	}

	@ApiOperation(value = "List all Customers", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Customers"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/orders/{id}")
	public Order updateOrder(Principal principal, @RequestBody List<OrderProduct> orderProducts, @PathVariable Long id)
			throws Exception {
		logger.info("***updateOrder method accessed by " + principal.getName() + "***");

		Account user = AccountUtil.getAccount(principal.getName());
		Customer customer = null;
		Order order = orderService.findOrder(id);

		if (order != null) {
			if (user.getRole() == Role.CUSTOMER) {
				customer = accountService.findCustomer(user.getAccountId());
			}
		} else {
			throw new Exception("This Order does not exist yet.  Please use the Post method to create a new order.");
		}

		if ((customer != null && customer.getOrders().contains(order))
				|| user.getRole() == Role.ADMIN) {

			return orderService.saveOrder(customer, orderProducts);

		} else {
			throw new Exception("You can only update your own orders unless you are an Administrator.");
		}

	}


	@ApiOperation(value = "List all Customers", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Customers"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/orders/{id}")
	public void deleteOrder(Principal principal, @PathVariable Long id) throws Exception {

		logger.info("***deleteOrder method accessed by " + principal.getName() + "***");

		Account user = AccountUtil.getAccount(principal.getName());
		Customer customer = null;
		Order order = orderService.findOrder(id);

		if (order != null) {
			if (user.getRole() == Role.CUSTOMER) {
				customer = accountService.findCustomer(user.getAccountId());
			}
		} else {
			throw new Exception("This Order does not exist.");
		}

		if ((customer != null && customer.getOrders().contains(order))
				|| user.getRole() == Role.ADMIN) {

			orderService.deleteOrder(id);

		} else {
			throw new Exception("You can only delete your own orders unless you are an Administrator.");
		}

	}

	@ApiOperation(value = "Delete OrderProduct from Order", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Successfully delete OrderProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/orders/{oid}/order-products/{pid}")
	public void deleteOrderProduct(Principal principal, @PathVariable Long oid, @PathVariable Long pid)
			throws Exception {

		logger.info("***deleteOrder method accessed by " + principal.getName() + "***");

		Account user = AccountUtil.getAccount(principal.getName());
		Customer customer = null;
		Order order = orderService.findOrder(oid);

		if (order != null) {
			if (user.getRole() == Role.CUSTOMER) {
				customer = accountService.findCustomer(user.getAccountId());
			}
		} else {
			throw new Exception("This Order does not exist.");
		}

		if ((customer != null && customer.getOrders().contains(order))
				|| user.getRole() == Role.ADMIN) {

			orderService.deleteOrderProduct(oid, pid);

		} else {
			throw new Exception(
					"You can only delete orderProducts from your own orders unless you are an Administrator.");
		}

	}


}
