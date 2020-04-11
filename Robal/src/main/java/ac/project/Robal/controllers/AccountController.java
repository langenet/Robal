package ac.project.Robal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.models.Account;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Owner;
import ac.project.Robal.services.AccountService;

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
	public Account saveCustomer(@RequestBody Customer customer) throws Exception {
		return accountService.saveCustomer(customer);
	}

	@PostMapping("/owners")
	public Account saveOwner(@RequestBody Owner owner) throws Exception {
		return accountService.saveOwner(owner);
	}
	@PostMapping("/administrators")
	public Account saveAdministrator(@RequestBody Administrator adminsitrator) throws Exception {
		return accountService.saveAdministrator(adminsitrator);
	}
	
	
	@PutMapping("/customers/{id}")
	public Account updateCustomer(@RequestBody Customer customer) throws Exception {
		return accountService.saveCustomer(customer);
	}
	@PutMapping("/owners/{id}")
	public Account updateOwner(@RequestBody Owner owner) throws Exception {
		return accountService.saveOwner(owner);
	}
	@PutMapping("/administrators/{id}")
	public Account updateAdministrator(@RequestBody Administrator adminsitrator) throws Exception {
		return accountService.saveAdministrator(adminsitrator);
	}


//	@GetMapping("/accounts/{id}")
//	public Account findAccount(@PathVariable Long id) {
//		return accountService.find(id);
//	}

//	@PutMapping("/accounts/{id}")
//	public Account updateAccount(@PathVariable Long id, @RequestBody Account account) throws Exception {
//		if (id.equals(account.getAccountId())) {
//			return accountService.save(account);
//		} else {
//			throw new Exception("Path error. mismatch Account id");
//		}
//	}

//	@DeleteMapping("/accounts/{id}")
//	public void deleteAccount(@PathVariable Long id) {
//		accountService.delete(id);
//	}

}
