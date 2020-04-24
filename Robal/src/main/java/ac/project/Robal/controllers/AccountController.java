package ac.project.Robal.controllers;

import java.net.URI;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Owner;
import ac.project.Robal.services.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;

//To-Do: Add response codes for all endpoints.
//To-Do: Add Get and Delete Mappings for single account and for list.
//To-Do: Why is the account table not being created?
@RestController
public class AccountController{

	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	// Customers
	@ApiOperation(value = "Create an account", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PostMapping("/customers")
	public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) throws Exception {
		Customer result = accountService.saveCustomer(customer);
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);
	}

	@GetMapping("/customers/{id}")
	public Customer findCustomer(@PathVariable Long id) throws NotFoundException {
		return accountService.findCustomer(id);
	}

//	@PreAuthorize("ADMIN")
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(Principal principal, @PathVariable Long id) throws NotFoundException {
		accountService.deleteCustomer(id);
	}

	@PutMapping("/customers/{id}")
	public Customer updateCustomer(@RequestBody Customer customer) throws Exception {
		return accountService.saveCustomer(customer);
	}

	// Owners
	@PostMapping("/owners")
	public Owner saveOwner(@RequestBody Owner owner) throws Exception {
		return accountService.saveOwner(owner);
	}

	@GetMapping("/owners/{id}")
	public Owner findOwner(@PathVariable Long id) throws NotFoundException {
		return accountService.findOwner(id);
	}

	@DeleteMapping("/owners/{id}")
	public void deleteOwner(@PathVariable Long id) throws NotFoundException {
		accountService.deleteOwner(id);
	}

	@PutMapping("/owners/{id}")
	public Owner updateOwner(@RequestBody Owner owner) throws Exception {
		return accountService.saveOwner(owner);
	}

	// Administratrators
	@PostMapping("/administrators")
	public Administrator saveAdministrator(@RequestBody Administrator adminsitrator) throws Exception {
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

	@PutMapping("/administrators/{id}")
	public Administrator updateAdministrator(@RequestBody Administrator adminsitrator) throws Exception {
		return accountService.saveAdministrator(adminsitrator);
	}

}
