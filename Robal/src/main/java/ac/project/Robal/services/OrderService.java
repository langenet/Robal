package ac.project.Robal.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.controllers.AccountController;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OrderProductRepository;
import ac.project.Robal.repositories.OrderRepository;
import ac.project.Robal.repositories.StoreProductRepository;
import javassist.NotFoundException;

@Service
public class OrderService {

	Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	private final static double GST = 1.13;

	private OrderRepository orderRepository;
	private CustomerRepository customerRepository;
	private OrderProductRepository orderProductRepository;
	private StoreProductRepository storeProductRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
			OrderProductRepository orderProductRepository, StoreProductRepository storeProductRepository) {
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.orderProductRepository = orderProductRepository;
		this.storeProductRepository = storeProductRepository;
	}

	public Order findOrder(Long id) {
		logger.info("***findOrder by id: " + id + "***");
		return orderRepository.findById(id).orElse(null);
	}

	public List<Order> findOrders() {
		logger.info("***OrderService: findOrders (ALL)***");
		return orderRepository.findAll();
	}

	public List<OrderProduct> findOrderProducts() {
		logger.info("***Repo:findOrderProducts***");
		return orderProductRepository.findAll();
	}

	// TODO is there missing a findOrderProduct by ID to return a single order
	// product?

	public Order saveOrder(Customer customer, List<OrderProduct> orderProducts) throws Exception {
		/*
		 * For each order product received from the user, we retrieve each product from
		 * the db via id. We do this to ensure that the product actually exists. If this
		 * cascades, the user would be creating product! So that shouldn't happen.
		 * Furthermore, by going through the repository, product entity gets attached.
		 * Obviously the product must exist in the db beforehand.
		 */

		double subTotal = 0.0;

		List<StoreProduct> dbStoreProducts = new ArrayList<>();

		for (OrderProduct orderProduct : orderProducts) {

			StoreProduct storeProduct = storeProductRepository
					.findById(orderProduct.getStoreProduct().getStoreProductid()).orElseThrow(storeProductNotFound());

			if (storeProduct.getInventory() - orderProduct.getQuantity() >= 0) {
				storeProduct.setInventory(storeProduct.getInventory() - orderProduct.getQuantity());
				dbStoreProducts.add(storeProduct);

			} else {
				logger.info("***OrderService: Inventory alert < purchase quantity***");
				throw new Exception("Not enough inventory in stock to purchase " + storeProduct.getProduct().getName());
			}

			orderProduct = OrderProduct.builder()
					.orderProductId(orderProduct.getOrderProductId())
					.storeProduct(storeProduct)
					.price(storeProduct.getPrice())
					.quantity(orderProduct.getQuantity())
					.build();

			subTotal += orderProduct.getPrice() * orderProduct.getQuantity();
		}

		orderProducts = orderProductRepository.saveAll(orderProducts);

		BigDecimal totalPrice = BigDecimal.valueOf(subTotal * GST);
		totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);

		Order order = Order.builder().orderProducts(orderProducts).purchaseDate(LocalDate.now())
				.invoiceNumber((long) new Random().nextInt(999999)) // number between +1 and +999999
				.subTotal(subTotal).total(totalPrice.doubleValue()).build();
		order = orderRepository.save(order);

		customer.getOrders().add(order);
		logger.info("***Repo:saveOrder by customer: " + customer.getEmail() + "***");
		customerRepository.save(customer);

		return order;
	}


	public void deleteOrder(Long id) throws NotFoundException {
		logger.info("***Repo:deleteOrder by id: " + id + "***");
		orderRepository.delete(orderRepository.findById(id).orElseThrow(orderNotFound()));
	}

	public void deleteOrderProduct(Long orderId, Long orderProductId) throws NotFoundException {

		Order order = orderRepository.findById(orderId).orElseThrow(orderNotFound());

		// TODO This needs to be tested.
		order.getOrderProducts().removeIf(orderProduct -> orderProductId.equals(orderProduct.getOrderProductId()));
		logger.info("***deleteOrderProduct by orderId " + orderId + ", orderProductId " + orderProductId + "***");
		orderRepository.save(order);
	}

	private Supplier<NotFoundException> orderNotFound() {
		return () -> {
			logger.info("***orderNotFound Exception***");
			return new NotFoundException("The order was not found.");
		};
	}

	private Supplier<NotFoundException> storeProductNotFound() {
		return () -> {
			logger.info("***storeProductNotFound Exception***");
			return new NotFoundException("The StoreProduct does not exist.");
		};
	}

}
