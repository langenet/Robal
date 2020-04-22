package ac.project.Robal.services;

import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OrderProductRepository;
import ac.project.Robal.repositories.OrderRepository;
import ac.project.Robal.repositories.ProductRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class OrderService {

	private final static double GST = 1.13;

	private OrderRepository orderRepository;
	private CustomerRepository customerRepository;
	private OrderProductRepository orderProductRepository;
	private ProductRepository productRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository,
						CustomerRepository customerRepository,
						OrderProductRepository orderProductRepository,
						ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.orderProductRepository = orderProductRepository;
		this.productRepository = productRepository;
	}

	public Order saveOrder(Customer customer, List<OrderProduct> orderProducts) throws Exception {
		/* For each order product received from the user, we retrieve each product from the db via id.
		 We do this to ensure that the product actually exists. If this cascades, the user would be creating product!
		 So that shouldn't happen. Furthermore, by going through the repository, product entity gets attached.
		 Obviously the product must exist in the db beforehand. */
		orderProducts = orderProducts.stream()
				.map(orderProduct -> OrderProduct.builder()
						.product(productRepository.findById(orderProduct.getProduct().getProductId())
							.orElse(null))
						.build())
				.collect(Collectors.toList());
		orderProducts = orderProductRepository.saveAll(orderProducts);

		double subTotal = orderProducts.stream()
				.mapToDouble(OrderProduct::getPrice)
				.sum();

		Order order = Order.builder()
				.orderProducts(orderProducts)
				.purchaseDate(LocalDate.now())
				.invoiceNumber(new Random().nextLong())
				.subTotal(subTotal)
				.total(subTotal * GST)
				.build();
//		order = orderRepository.save(order);

		customer.getOrders().add(order);
		customerRepository.save(customer);
		
		return customer.getOrders().get(customer.getOrders().size() - 1);
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
