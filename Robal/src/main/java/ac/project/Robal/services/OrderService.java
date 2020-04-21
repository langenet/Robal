package ac.project.Robal.services;

import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OrderProductRepository;
import ac.project.Robal.repositories.OrderRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@Service
public class OrderService {

	private final static double GST = 1.13;

	private OrderRepository orderRepository;
	private CustomerRepository customerRepository;
	private OrderProductRepository orderProductRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository,
						CustomerRepository customerRepository,
						OrderProductRepository orderProductRepository) {
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.orderProductRepository = orderProductRepository;
	}
	
	
	public Order saveOrder(Customer customer, List<OrderProduct> orderProducts) throws Exception {

		double subTotal = orderProducts.stream().mapToDouble(OrderProduct::getPrice).sum();

		Order order = Order.builder()
				.orderProducts(orderProducts)
				.purchaseDate(LocalDate.now())
				.invoiceNumber(new Random().nextLong())
				.subTotal(subTotal)
				.total(subTotal * GST)
				.build();
		order = orderRepository.save(order);

		customer.getOrders().add(order);
		customerRepository.save(customer);
		
		return order;
		//TODO: add validation on the mandatory fields
//		if (order.getInvoiceNumber().isEmpty() && order.getEmail().isEmpty()) {
//			throw new ClientException("Cannot create Customer without a name and email");
//		}
	}
	
	public Order findOrder(Long id) {
		return orderRepository.findById(id).orElse(null);
	}

	public void deleteOrder(Long id) throws NotFoundException {
		orderRepository.delete(orderRepository.findById(id).orElseThrow(orderNotFound()));
	}

	private Supplier<NotFoundException> orderNotFound() {
		return () -> new NotFoundException("The order was not found.");
	}
	
}
