package ac.project.Robal.services;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import ac.project.Robal.models.Account;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OwnerRepository;

@Service
public class AccountDetailService implements UserDetailsService {

	private CustomerRepository customerRepository;
	private OwnerRepository ownerRepository;
	private AdministratorRepository administratorRepository;

	@Autowired
	public AccountDetailService(CustomerRepository customerRepository, OwnerRepository ownerRepository,
			AdministratorRepository administratorRepository) {
		this.customerRepository = customerRepository;
		this.ownerRepository = ownerRepository;
		this.administratorRepository = administratorRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

		Account user = customerRepository.findByEmail(s).orElseThrow(accountNotFound("Customer"));
		String role;

		if (user == null) {

			user = ownerRepository.findByEmail(s).orElseThrow(accountNotFound("Owner"));
			role = "OWNER";
		} else {
			role = "CUSTOMER";
		}

		if (user == null) {

			user = administratorRepository.findByEmail(s).orElseThrow(accountNotFound("Administrator"));
			role = "ADMIN";
		}

		// Figure out the user's actual role
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(simpleGrantedAuthority);

		return new User(user.getEmail(), 
						user.getPassword(), 
						true, 
						true, 
						true, 
						true, 
						authorities); // authorities);

	}
	
	private Supplier<UsernameNotFoundException> accountNotFound(String accountType){
		return () -> new UsernameNotFoundException( accountType + " Not Found.");
	}

}
