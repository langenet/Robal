package ac.project.Robal.controllers;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@ApiOperation(value = "Find an Order by id", response = Order.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Order Found"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@GetMapping("/orders/{id}")
	public ResponseEntity<Order> findOrder(@PathVariable Long id) {
		logger.info("***findOrder by id method accessed***");
		return new ResponseEntity<>(orderService.findOrder(id), HttpStatus.OK);
	}

	@ApiOperation(value = "List all Orders", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Orders found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/orders/")
	public ResponseEntity<List<Order>> listOrders() {
		logger.info("***listOrders method accessed***");
		return new ResponseEntity<>(orderService.findOrders(), HttpStatus.OK);
	}

	@ApiOperation(value = "List all Order Products", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Order Products found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/order-products/")
	public ResponseEntity<List<OrderProduct>> listOrderProducts() {
		logger.info("***listOrderProducts method accessed***");
		return new ResponseEntity<>(orderService.findOrderProducts(), HttpStatus.OK);
	}

	@ApiOperation(value = "Save an Order", response = Order.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully saved Order."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('CUSTOMER')")
	@PostMapping("/orders")
	public ResponseEntity<Order> saveOrder(Principal principal, @RequestBody List<OrderProduct> orderProducts) throws Exception {
		logger.info("***saveOrder method accessed by " + principal.getName() + "***");

		Customer customer = null;
		customer = accountService.findCustomerByEmail(principal.getName());
		return ResponseEntity.created(new URI("/orders")).body(orderService.saveOrder(customer, orderProducts));

	}

	@ApiOperation(value = "Update Order by Id", response = Order.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully updated order"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/orders/{id}")
	public ResponseEntity<Order> updateOrder(Principal principal, @RequestBody List<OrderProduct> orderProducts,
			@PathVariable Long id)
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
			order = orderService.saveOrder(customer, orderProducts);
			return ResponseEntity.created(new URI("/orders/" + order.getOrderId())).body(order);

		} else {
			throw new Exception("You can only update your own orders unless you are an Administrator.");
		}

	}


	@ApiOperation(value = "Delete order by Id", response = Order.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted order"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/orders/{id}")
	public ResponseEntity deleteOrder(Principal principal, @PathVariable Long id) throws Exception {

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
			return ResponseEntity.noContent().build();

		} else {
			throw new Exception("You can only delete your own orders unless you are an Administrator.");
		}

	}

	@ApiOperation(value = "Delete OrderProduct from Order", response = Order.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Successfully delete OrderProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/orders/{oid}/order-products/{pid}")
	public ResponseEntity deleteOrderProduct(Principal principal, @PathVariable Long oid, @PathVariable Long pid)
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
			return ResponseEntity.noContent().build();

		} else {
			throw new Exception(
					"You can only delete orderProducts from your own orders unless you are an Administrator.");
		}

	}


}
