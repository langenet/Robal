package ac.project.Robal.services;

import org.springframework.beans.factory.annotation.Autowired;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.repositories.OrderRepository;

public class OrderService {
	
	private OrderRepository orderRepository;

	@Autowired
	public OrderService(OrderRepository )
	public static Order saveOrder(Order order, Customer customer) throws Exception {
		if (customer.getName().isEmpty() && customer.getEmail().isEmpty()) {
			throw new ClientException("Cannot create Customer without a name and email");
		}
		return orderRepository.save(order, customer);
	}

}
