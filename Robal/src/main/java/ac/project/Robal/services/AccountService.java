package ac.project.Robal.services;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Autowired
	public AccountService(CustomerRepository customerRepository, 
							OwnerRepository ownerRepository,
							AdministratorRepository administratorRepository,
							BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.customerRepository = customerRepository;
		this.ownerRepository = ownerRepository;
		this.administratorRepository = administratorRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;

	}

	// Customer
	public Customer saveCustomer(Customer customer) throws ClientException, NotFoundException {
		if (customer.getName() == null 
				|| customer.getEmail() == null
				|| customer.getPassword() == null) {
			//TODO Logging
			//TODO jUnit test saveCustomer null values
			throw new ClientException("Cannot create or update customer without Name, Email or Password.");
		}
		if ((customer.getAccountId() == null ||customer.getAccountId() == 0)) {
			customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
			return customerRepository.save(customer);
		} else {
			//TODO jUnit test update customer
			Customer dbCustomer = customerRepository.findById(customer.getAccountId()).orElseThrow(accountNotFound());
			
			dbCustomer.setName(customer.getName());
			dbCustomer.setBillingAddress(customer.getBillingAddress());
			dbCustomer.setEmail(customer.getEmail());
			dbCustomer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));	
			dbCustomer.setRole(customer.getRole());
			dbCustomer.setPaymentMethod(customer.getPaymentMethod());

			//TODO test that this does not wipe out any orders or duplicate them
			//TODO validation that order is complete
			for(Order order:customer.getOrders()) {
				if (!dbCustomer.getOrders().contains(order)) {
					dbCustomer.getOrders().add(order);
				}
			}
			
			return customerRepository.save(dbCustomer);
		}
	}

	public Customer findCustomer(Long id) throws NotFoundException {
		return customerRepository.findById(id).orElseThrow(accountNotFound());
	}

	public void deleteCustomer(Long id) throws NotFoundException {
		customerRepository.delete(customerRepository.findById(id).orElseThrow(accountNotFound()));
	}

	// Owner
	public Owner saveOwner(Owner owner) throws Exception {			
		if (owner.getName() == null 
				|| owner.getEmail() == null
				|| owner.getPassword() == null) {
			//TODO Logging
			//TODO jUnit test saveowner null values
			throw new ClientException("Cannot create or update owner without Name, Email or Password.");
		}
		if ((owner.getAccountId() == null ||owner.getAccountId() == 0)) {
			owner.setPassword(bCryptPasswordEncoder.encode(owner.getPassword()));
			return ownerRepository.save(owner);
		} else {
			//TODO jUnit test update owner
			Owner dbOwner = ownerRepository.findById(owner.getAccountId()).orElseThrow(accountNotFound());
			
			dbOwner.setName(owner.getName());
			dbOwner.setEmail(owner.getEmail());
			dbOwner.setPassword(bCryptPasswordEncoder.encode(owner.getPassword()));	
			dbOwner.setRole(owner.getRole());
		

			//TODO test that this does not wipe out any orders or duplicate them
			//TODO validation that order is complete
	
			return ownerRepository.save(dbOwner);
		}
	}
	

	public Owner findOwner(Long id) throws NotFoundException {

		return ownerRepository.findById(id).orElseThrow(accountNotFound());
	}

	public Owner findOwnerByEmail(String email) throws NotFoundException {
		return ownerRepository.findByEmail(email).orElseThrow(accountNotFound());
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
		//TODO Logging
		return () -> new NotFoundException("The account was not found.");
	}
}