package ac.project.Robal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OrderRepository;

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
	
	
	public Order saveOrder(Order order, Customer customer) throws Exception {
		if (customer.getName().isEmpty() && customer.getEmail().isEmpty()) {
			throw new ClientException("Cannot create Customer without a name and email");
		}
		return orderRepository.save(order);
	}

}
