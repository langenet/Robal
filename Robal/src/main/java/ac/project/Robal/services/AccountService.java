package ac.project.Robal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Account;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Owner;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OwnerRepository;

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

	public Account save(Account account) throws Exception {
		if (account.getAccountId() != null && account.getName().isEmpty() && account.getEmail().isEmpty()) {
			throw new ClientException("Cannot create account without a name and email");
		}

		if (account.getAccountType().equals("Customer")) {
			Customer customer = new Customer(account);
			return customerRepository.save(customer);

		} else if (account.getAccountType().equals("Owner")) {
			Owner owner = new Owner(account);
			return ownerRepository.save(owner);
			
		} else if (account.getAccountType().equals("Admin")) {
			Administrator administrator = new Administrator(account);
			return administratorRepository.save(administrator);

		} else {
			throw new Exception("Path error. mismatch Account id");
		}
	}
}