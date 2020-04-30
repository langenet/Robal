package ac.project.Robal.controllers;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.enums.Role;
import ac.project.Robal.models.Account;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Owner;
import ac.project.Robal.services.AccountService;
import ac.project.Robal.utils.AccountUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;

//To-Do: Add response codes for all endpoints.
//To-Do: Add Get and Delete Mappings for single account and for list.
//To-Do: Why is the account table not being created?
@RestController
public class AccountController {

	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	// Customers

	@ApiOperation(value = "Find a Customer", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Customer"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> findCustomer(Principal principal, @PathVariable Long id)
			throws Exception, NotFoundException {
		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(accountService.findCustomer(id), HttpStatus.OK);
		} else {
			throw new Exception("You are not authorized to view");
		}
	}

	@ApiOperation(value = "List all Customers", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Customers"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/customers/")
	public ResponseEntity<List<Customer>> listCustomers(Principal principal) throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(accountService.listCustomers(), HttpStatus.OK);
		} else {

			throw new Exception("You are not authorized to view");
		}
	}

	@ApiOperation(value = "Create a Customer", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Customer"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PostMapping("/customers")
	public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) throws Exception {
		Customer result;

		if (customer.getAccountId() == null
				|| customer.getAccountId() == 0) {
			result = accountService.saveCustomer(customer);
		} else {
			throw new Exception("Please use the Put method to update a Customer.");
		}

		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);
	}

	@ApiOperation(value = "Update a Customer", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(Principal principal, @RequestBody Customer customer)
			throws Exception {
		Account user = AccountUtil.getAccount(principal.getName());
		Customer result;

		if (user.getAccountId() == customer.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveCustomer(customer);

		} else {

			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);

	}

	@ApiOperation(value = "Delete a Customer", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted Customer"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(Principal principal, @PathVariable Long id) throws NotFoundException {

		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) {
			accountService.deleteCustomer(id);
		}
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

//TODO add PreAuth for "OWNER"
	@DeleteMapping("/owners/{id}")
	public void deleteOwner(@PathVariable Long id) throws NotFoundException {
		accountService.deleteOwner(id);
	}

//TODO add PreAuth for "OWNER"
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
