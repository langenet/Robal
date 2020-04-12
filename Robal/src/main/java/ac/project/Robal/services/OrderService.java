package ac.project.Robal.services;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OrderRepository;
import javassist.NotFoundException;

@Service
public class OrderService {
	
	private OrderRepository orderRepository;
	private CustomerRepository customerRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository,
						CustomerRepository customerRepository) {
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
	}
	
	
	public Order saveOrder(Order order) throws Exception {
		
		//TODO: add validation on the mandatory fields
//		if (order.getInvoiceNumber().isEmpty() && order.getEmail().isEmpty()) {
//			throw new ClientException("Cannot create Customer without a name and email");
//		}
		return orderRepository.save(order);
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
