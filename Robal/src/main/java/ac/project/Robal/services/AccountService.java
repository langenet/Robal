package ac.project.Robal.services;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.Owner;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OwnerRepository;
import javassist.NotFoundException;

@Service
public class AccountService {

	private CustomerRepository customerRepository;
	private OwnerRepository ownerRepository;
	private AdministratorRepository administratorRepository;

	@Autowired
	public AccountService(CustomerRepository customerRepository, OwnerRepository ownerRepository,
			AdministratorRepository administratorRepository) {
		this.customerRepository = customerRepository;
		this.ownerRepository = ownerRepository;
		this.administratorRepository = administratorRepository;
	}

	// Customer
	public Customer saveCustomer(Customer customer) throws Exception {
		// Removed null check on iD but maybe that's needed?
		if (customer.getName().isEmpty() && customer.getEmail().isEmpty()) {
			throw new ClientException("Cannot create Customer without a name and email");
		}
		return customerRepository.save(customer);
	}

	public Customer newCustomerOrder(Order order, Long id) throws Exception {
		// Removed null check on iD but maybe that's needed?
		Customer customer = findCustomer(id);
//		customer.getOrders().add(order);

		if (customer.getName().isEmpty() && customer.getEmail().isEmpty()) {
			throw new ClientException("Cannot create Customer without a name and email");
		}
		return customerRepository.save(customer);
	}

	public Customer findCustomer(Long id) {
		return customerRepository.findById(id).orElse(null);
	}

	public void deleteCustomer(Long id) throws NotFoundException {
		customerRepository.delete(customerRepository.findById(id).orElseThrow(accountNotFound()));
	}

	// Owner
	public Owner saveOwner(Owner owner) throws Exception {
		// Removed null check on iD but maybe that's needed?
		if (owner.getName().isEmpty() && owner.getEmail().isEmpty()) {
			throw new ClientException("Cannot create Owner without a name and email");
		}
		return ownerRepository.save(owner);
	}

	public Owner findOwner(Long id) {

		return ownerRepository.findById(id).orElse(null);
	}

	public void deleteOwner(Long id) throws NotFoundException {
		ownerRepository.delete(ownerRepository.findById(id).orElseThrow(accountNotFound()));
	}

	// Administrator
	public Administrator saveAdministrator(Administrator administrator) throws Exception {
		// Removed null check on iD but maybe that's needed?
		if (administrator.getName().isEmpty() && administrator.getEmail().isEmpty()) {
			throw new ClientException("Cannot create Administrator without a name and email");
		}
		return administratorRepository.save(administrator);
	}

	public Administrator findAdministrator(Long id) {
		return administratorRepository.findById(id).orElse(null);
	}

	public void deleteAdministrator(Long id) throws NotFoundException {
		administratorRepository.delete(administratorRepository.findById(id).orElseThrow(accountNotFound()));
	}

	private Supplier<NotFoundException> accountNotFound() {
		return () -> new NotFoundException("The account was not found.");
	}
}