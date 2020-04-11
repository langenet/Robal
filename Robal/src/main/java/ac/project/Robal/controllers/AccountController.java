package ac.project.Robal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.models.Account;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Owner;
import ac.project.Robal.services.AccountService;
import javassist.NotFoundException;

//To-Do: Add response codes for all endpoints.
//To-Do: Add Get and Delete Mappings for single account and for list.
//To-Do: Why is the account table not being created?
@RestController
public class AccountController<A extends Account> {
	
	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/customers")
	public Customer saveCustomer(@RequestBody Customer customer) throws Exception {
		return accountService.saveCustomer(customer);
	}

	@PostMapping("/owners")
	public Owner saveOwner(@RequestBody Owner owner) throws Exception {
		return accountService.saveOwner(owner);
	}
	@PostMapping("/administrators")
	public Administrator saveAdministrator(@RequestBody Administrator adminsitrator) throws Exception {
		return accountService.saveAdministrator(adminsitrator);
	}
	
	
	@PutMapping("/customers/{id}")
	public Customer updateCustomer(@RequestBody Customer customer) throws Exception {
		return accountService.saveCustomer(customer);
	}
	@PutMapping("/owners/{id}")
	public Owner updateOwner(@RequestBody Owner owner) throws Exception {
		return accountService.saveOwner(owner);
	}
	@PutMapping("/administrators/{id}")
	public Administrator updateAdministrator(@RequestBody Administrator adminsitrator) throws Exception {
		return accountService.saveAdministrator(adminsitrator);
	}


	@GetMapping("/administrators/{id}")
	public Administrator findAdministrator(@PathVariable Long id) {
	return accountService.findAdministrator(id);
	}



	@DeleteMapping("/administrators/{id}")
	public void deleteAdministrator(@PathVariable Long id) throws NotFoundException {
	accountService.deleteAdministrator(id);
	}

	@GetMapping("/owners/{id}")
	public Owner findOwner(@PathVariable Long id) {
	return accountService.findOwner(id);
	}



	@DeleteMapping("/owners/{id}")
	public void deleteOwner(@PathVariable Long id) throws NotFoundException {
	accountService.deleteOwner(id);
	}

	@GetMapping("/customers/{id}")
	public Customer findCustomer(@PathVariable Long id) {
	return accountService.findCustomer(id);
	}



	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable Long id) throws NotFoundException {
	accountService.deleteCustomer(id);
	}
}
