package ac.project.Robal.controllers;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	private AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	// Find a Customer
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
		logger.info("***Get Customer by ID method accessed by " + user.getEmail() + "***");
		
		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(accountService.findCustomer(id), HttpStatus.OK);
		} else {
			logger.info("***findCustomer method failed: " + user.getEmail() + "" + user.getRole() + "***");
			throw new Exception("You are not authorized to view");
		}
	}

	
	//List all Customers
	@ApiOperation(value = "List all Customers", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Customers"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> listCustomers(Principal principal) throws Exception {
		
		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("******List Customer method accessed by " + user.getEmail() + "***");

		if (user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(accountService.listCustomers(), HttpStatus.OK);
		} else {
			logger.info("******List Customer - Need ADMIN role to list cusomters. Accessed by " + user.getEmail() + " role: " + user.getRole() + "***");
			throw new Exception("You are not authorized to view");
		}
	}

	
	//Create Customer
	@ApiOperation(value = "Create a Customer", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Customer"),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PostMapping("/customers")
	public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) throws Exception {
		
		logger.info("***saveCustomer method accessed by " + customer.getName() + "***");
		Customer result;

		
		
		if (customer.getAccountId() == null
				|| customer.getAccountId() == 0) {
			result = accountService.saveCustomer(customer);
		} else {
			throw new Exception("Please use the Put method to update a Customer.");
		}

		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);
	}

	
	//Update a Customer
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
		logger.info("***updateCustomer method accessed by " + user.getEmail() + "***");
		
		Customer result;

		if (user.getAccountId() == customer.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveCustomer(customer);

		} else {
			logger.info("***updateCustomer method failed. No priviledge for: " + user.getEmail() + " role: " + user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);

	}

	
	//Delete a Customer
	@ApiOperation(value = "Delete a Customer", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted Customer"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(Principal principal, @PathVariable Long id) throws NotFoundException {
		logger.info("***deleteCustomer method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) {
			accountService.deleteCustomer(id);
		}
	}

	
	// Find Owner
	@ApiOperation(value = "Find an owner", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Owner"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNERS')")
	@GetMapping("/owners/{id}")
	public ResponseEntity<Owner> findOwner(Principal principal, @PathVariable Long id)
			throws Exception, NotFoundException {
		
		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***findOwner method accessed by " + user.getEmail() + "***");

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) { 
			return new ResponseEntity<>(accountService.findOwner(id), HttpStatus.OK);
		} else {
			logger.info("***findOwner method failed: " + user.getEmail() + " " + user.getRole() + "***");
			throw new Exception("You are not authorized to view");
		}
	}

	
	// List all owners
	@ApiOperation(value = "List all Owners", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Owners"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNERS')")
	@GetMapping("/owners")
	public ResponseEntity<List<Owner>> listOwners(Principal principal) throws Exception {
		logger.info("***listOwners method accessed " + " by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());
		
		if (user.getRole() == Role.ADMIN) { //admins should be able to check right?
			return new ResponseEntity<>(accountService.listOwners(), HttpStatus.OK);
		} else {

			throw new Exception(user.getName() + " is not authorized to view");
		}
	}

	
	//Update Owner
	@ApiOperation(value = "Update an Owner", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PutMapping("/owners/{id}")
	public ResponseEntity<Owner> updateOwner(Principal principal, @RequestBody Owner owner)
			throws Exception {
		logger.info("***updateOwner method accessed " + " by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());
		Owner result;

		if (user.getAccountId() == owner.getAccountId()
				|| user.getRole() == Role.ADMIN ) {

			result = accountService.saveOwner(owner);

		} else {
			logger.info("***updateOwner method failed. No priviledge for: " + user.getEmail() + " role: " + user.getRole() + "***");
			throw new Exception("Only the owner themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/owners/" + result.getAccountId())).body(result);

	}
	

	//Delete Owner
	@ApiOperation(value = "Delete an Owner", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted Owner account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@DeleteMapping("/owners/{id}")
	public void deleteOwner(Principal principal, @PathVariable Long id) throws NotFoundException {
		logger.info("***deleteOwner method accessed " + " by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN ) {
			accountService.deleteOwner(id);
		}
	}


	// Add Administratrator	
	@ApiOperation(value = "Create an Administrator", response = Administrator.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Administrator"),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PostMapping("/administrators")
	public ResponseEntity<Administrator> saveAdministrator(@RequestBody Administrator administrator) throws Exception {
		logger.info("***saveAdministrator method accessed by " + administrator.getName() + "***");
		Administrator result;
	
		if (administrator.getAccountId() == null
				|| administrator.getAccountId() == 0) {
			result = accountService.saveAdministrator(administrator);
		} else {
			throw new Exception("Please use the Put method to update a Customer.");
		}

		return ResponseEntity.created(new URI("/administrator/" + result.getAccountId())).body(result);
	}


	
	// Find Administrator
	@ApiOperation(value = "Find a Administrator", response = Administrator.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Administrator"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/admins/{id}")
	public ResponseEntity<Administrator> findAdministrator(Principal principal, @PathVariable Long id)
			throws Exception, NotFoundException {
		logger.info("***findAdministrator method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(accountService.findAdministrator(id), HttpStatus.OK);
		} else {
			throw new Exception("You are not authorized to view");
		}
	}

	
	//List all Administrators
	@ApiOperation(value = "List all Adminstrators", response = Administrator.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Administrator"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admins")
	public ResponseEntity<List<Administrator>> listAdministrators(Principal principal) throws Exception {
		logger.info("***listAdministrator method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(accountService.listAdministrators(), HttpStatus.OK);
		} else {

			throw new Exception("You are not authorized to view these accounts.");
		}
	}



	//Update Administrator
	@ApiOperation(value = "Update an Administrator", response = Administrator.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/administrators/{id}")
	public ResponseEntity<Administrator> updateAdministrator(Principal principal, @RequestBody Administrator administrator)
			throws Exception {
		logger.info("***updateAdministrator method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());
		Administrator result;

		if (user.getAccountId() == administrator.getAccountId()
				|| user.getRole() == Role.ADMIN ) {  //seems redundant here!

			result = accountService.saveAdministrator(administrator);

		} else {

			throw new Exception("Only an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/administrators/" + result.getAccountId())).body(result);

	}


	//Delete Owner
	@ApiOperation(value = "Delete an Administrator", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted Administrator account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/administrators/{id}")
	public void deleteAdministrator(Principal principal, @PathVariable Long id) throws NotFoundException {
		logger.info("***deleteAdministrator method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN ) { //seems redundant here!
			accountService.deleteOwner(id);
		}
	}
}
