package ac.project.Robal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.enums.AccountType;
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

	public Customer saveCustomer(Customer customer) throws Exception {
		//Removed null check on iD but maybe that's needed?	
		if (customer.getName().isEmpty() && customer.getEmail().isEmpty()) {
			throw new ClientException("Cannot create Customer without a name and email");
		}
		return customerRepository.save(customer);
	}

	public Owner saveOwner(Owner owner) throws Exception {
		//Removed null check on iD but maybe that's needed?	
		if (owner.getName().isEmpty() && owner.getEmail().isEmpty()
				&& owner.getStore() != null) {
			throw new ClientException("Cannot create Owner without a name and email");
		}
		return ownerRepository.save(owner);
	}

	public Administrator saveAdministrator(Administrator administrator) throws Exception{
		//Removed null check on iD but maybe that's needed?	
		if(administrator.getName().isEmpty() 
				&& administrator.getEmail().isEmpty()) {
				throw new ClientException("Cannot create Administrator without a name and email");
			}
		
		return administratorRepository.save(administrator);
	}

//	public Account save(Account account) throws Exception {
//		if (account.getAccountId() != null && account.getName().isEmpty() && account.getEmail().isEmpty()) {
//			throw new ClientException("Cannot create account without a name and email");
//		}
//
//		if (account.getAccountType().equals(AccountType.CUSTOMER)) {
//			Customer customer = new Customer(account);
//			return customerRepository.save(customer);
//
//		} else if (account.getAccountType().equals(AccountType.OWNER)) {
//			Owner owner = new Owner(account);
//			return ownerRepository.save(owner);
//		} else if (account.getAccountType().equals(AccountType.ADMINISTRATOR)) {
//			Administrator administrator = new Administrator(account);
//			return administratorRepository.save(administrator);
//
//		} 
//	}
}