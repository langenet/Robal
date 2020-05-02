package ac.project.Robal.utils;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ac.project.Robal.models.Account;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Owner;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OwnerRepository;
import lombok.Getter;

@Getter

@Component
public class AccountUtil {

	private static CustomerRepository customerRepository;
	private static OwnerRepository ownerRepository;
	private static AdministratorRepository administratorRepository;

	@Autowired
	public AccountUtil(CustomerRepository customerRepository, OwnerRepository ownerRepository,
			AdministratorRepository administratorRepository) {
		AccountUtil.customerRepository = customerRepository;
		AccountUtil.ownerRepository = ownerRepository;
		AccountUtil.administratorRepository = administratorRepository;
	}

	// TODO Andy, how can i return either a Customer, Owner or administrator with a
	// generic?
	// I've been trying <A extends Account> Account but it only allows me to create
	// Account type objects.
	public static <A extends Account> Account getAccount(String email) throws UsernameNotFoundException {

		Customer customer = customerRepository.findByEmail(email).orElse(null);
		Owner owner;
		Administrator administrator;
		
		if (customer == null) {

			owner = ownerRepository.findByEmail(email).orElse(null);

		} else {
			
			return customer;
		}

		if (owner == null) {

			administrator = administratorRepository.findByEmail(email).orElseThrow(accountNotFound());
			
		} else {
			
			return owner;
		}

		return administrator;
	}

	private static Supplier<UsernameNotFoundException> accountNotFound() {
		return () -> new UsernameNotFoundException("Account Not Found.");
	}
}
