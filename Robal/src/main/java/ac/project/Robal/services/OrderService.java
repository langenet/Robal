package ac.project.Robal.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OrderProductRepository;
import ac.project.Robal.repositories.OrderRepository;
import javassist.NotFoundException;

@Service
public class OrderService {
	
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
	
	
	public Order saveOrder(Order order) throws Exception {
		
		Customer customer = customerRepository.findById(order.getCustomer().getAccountId()).orElse(null);
		
		order.setCustomer(customer);
		
		order = orderRepository.save(order);

		//TODO Validate OrderProducts
		List<OrderProduct> orderProducts = new ArrayList<>(order.getOrderProducts());
		
		for(OrderProduct orderProduct:orderProducts) {
			orderProduct.setOrder(order);
		}
		
		orderProducts = orderProductRepository.saveAll(orderProducts);
		order.setOrderProducts(orderProducts);
		
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
