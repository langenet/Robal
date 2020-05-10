package ac.project.Robal.controllers;

import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import ac.project.Robal.models.Order;
import ac.project.Robal.models.Owner;
import ac.project.Robal.services.AccountService;
import ac.project.Robal.services.OrderService;
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
	private OrderService orderService;

	private Environment environment;

	@Autowired
	public AccountController(AccountService accountService,
			Environment environment,
			OrderService orderService) {
		this.accountService = accountService;
		this.orderService = orderService;
		this.environment = environment;
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

	@ApiOperation(value = "Find Customer Order", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Customer Order"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@GetMapping("/customers/{cid}/orders/{oid}")
	public ResponseEntity<Order> findCustomerOrder(Principal principal, @PathVariable Long cid,
			@PathVariable Long oid) throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("******View Customer Order method accessed by " + user.getEmail() + "***");

		if (user.getAccountId() == cid
				|| user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(orderService.findOrder(oid), HttpStatus.OK);
		} else {
			logger.info(
					"******View Customer Order - Need ADMIN role or you must be the Customer to access their order. Accessed by "
							+ user.getEmail()
							+ " role: " + user.getRole() + "***");
			throw new Exception("You are not authorized to view");
		}
	}

	// List all Customers
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

		return new ResponseEntity<>(accountService.listCustomers(), HttpStatus.OK);
	}

	@ApiOperation(value = "List all Customer Orders", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully found Customer Orders"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@GetMapping("/customers/{id}/orders")
	public ResponseEntity<List<Order>> listCustomerOrders(Principal principal, @PathVariable Long id) throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("******List Customer Orders method accessed by " + user.getEmail() + "***");

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) {
			return new ResponseEntity<>(accountService.listCustomerOrders(id), HttpStatus.OK);
		} else {
			logger.info(
					"******List Customer Orders - Need ADMIN role or you must be the Customer to access their orders. Accessed by "
							+ user.getEmail()
							+ " role: " + user.getRole() + "***");
			throw new Exception("You are not authorized to view");
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
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/owners")
	public ResponseEntity<List<Owner>> listOwners(Principal principal) throws Exception {
		logger.info("***listOwners method accessed " + " by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());

		return new ResponseEntity<>(accountService.listOwners(), HttpStatus.OK);

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

	// List all Administrators
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

		return new ResponseEntity<>(accountService.listAdministrators(), HttpStatus.OK);
	}

	// Create Customer
	@ApiOperation(value = "Create a Customer", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Customer"),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PostMapping("/customers")
	public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) throws Exception {

		logger.info("***saveCustomer method accessed by " + customer.getEmail() + "***");
		Customer result;

		if (customer.getAccountId() == null
				|| customer.getAccountId() == 0) {
			result = accountService.saveCustomer(customer);
		} else {
			throw new Exception("Please use the Put method to update a Customer.");
		}

		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);
	}

	// Update a Customer
	@ApiOperation(value = "Update whole Customer (all fields)", response = Customer.class)
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
			logger.info("***updateCustomer method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);

	}

	// update Customer Name

	@ApiOperation(value = "Update Customer name", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Customer name."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/customers/{id}/name")
	public ResponseEntity<Customer> updateCustomerName(Principal principal, @RequestBody Customer customer)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateCustomerName method accessed by " + user.getEmail() + "***");

		Customer result;

		if (user.getAccountId() == customer.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveCustomerName(customer);

		} else {
			logger.info("***updateCustomerName method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);

	}

	@ApiOperation(value = "Update Customer email", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Customer name."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/customers/{id}/email")
	public ResponseEntity<Customer> updateCustomerEmail(Principal principal, @RequestBody Customer customer)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateCustomerEmail method accessed by " + user.getEmail() + "***");

		Customer result;

		if (user.getAccountId() == customer.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveCustomerEmail(customer);

		} else {
			logger.info("***updateCustomerEmail method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId())).body(result);

	}

	@ApiOperation(value = "Update Customer password", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Customer name."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/customers/{id}/password")
	public ResponseEntity<Customer> updateCustomerPassword(Principal principal, @RequestBody Customer customer)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateCustomerPassword method accessed by " + user.getEmail() + "***");

		Customer result;

		if (user.getAccountId() == customer.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveCustomerPassword(customer);

		} else {
			logger.info("***updateCustomerPassword method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId() + "/password")).body(result);

	}

	@ApiOperation(value = "Update Customer Billing Address", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Customer Billing Address."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/customers/{id}/billing-address")
	public ResponseEntity<Customer> updateCustomerBillingAddress(Principal principal, @RequestBody Customer customer)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateCustomerBillingAddress method accessed by " + user.getEmail() + "***");

		Customer result;

		if (user.getAccountId() == customer.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveCustomerBillingAddress(customer);

		} else {
			logger.info(
					"***updateCustomerBillingAddress method failed. No priviledge for: " + user.getEmail() + " role: "
							+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId() + "/billing-address")).body(result);

	}

	@ApiOperation(value = "Update Customer payment method", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Customer payment method."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/customers/{id}/payment-method")
	public ResponseEntity<Customer> updateCustomerPaymentMethod(Principal principal, @RequestBody Customer customer)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateCustomerPaymentMethod method accessed by " + user.getEmail() + "***");

		Customer result;

		if (user.getAccountId() == customer.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveCustomerPaymentMethod(customer);

		} else {
			logger.info(
					"***updateCustomerPaymentMethod method failed. No priviledge for: " + user.getEmail() + " role: "
							+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId() + "/payment-method")).body(result);

	}

	// Update Owner
	@ApiOperation(value = "Create an Owner", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully create account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PostMapping("/owners")
	public ResponseEntity<Owner> saveOwner(@RequestBody Owner owner)
			throws Exception {
		logger.info("***saveOwner method accessed " + " by " + owner.getEmail() + "***");

		Owner result;

		result = accountService.saveOwner(owner);

		return ResponseEntity.created(new URI("/owners/" + result.getAccountId())).body(result);

	}

	// Update Owner
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
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveOwner(owner);

		} else {
			logger.info("***updateOwner method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the owner themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/owners/" + result.getAccountId())).body(result);

	}

	@ApiOperation(value = "Update Owner name", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Customer name."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PutMapping("/owners/{id}/name")
	public ResponseEntity<Owner> updateOwnerName(Principal principal, @RequestBody Owner owner)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateOwnerName method accessed by " + user.getEmail() + "***");

		Owner result;

		if (user.getAccountId() == owner.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveOwnerName(owner);

		} else {
			logger.info("***updateOwnerName method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/customers/" + result.getAccountId() + "/name")).body(result);

	}

	@ApiOperation(value = "Update Owner email", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Owner email."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PutMapping("/owners/{id}/email")
	public ResponseEntity<Owner> updateOwnerEmail(Principal principal, @RequestBody Owner owner)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateOwnerEmail method accessed by " + user.getEmail() + "***");

		Owner result;

		if (user.getAccountId() == owner.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveOwnerEmail(owner);

		} else {
			logger.info("***updateOwnerEmail method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/owners/" + result.getAccountId() + "/email")).body(result);

	}

	@ApiOperation(value = "Update Owner password", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Owner password."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PutMapping("/owners/{id}/password")
	public ResponseEntity<Owner> updateOwnerPassword(Principal principal, @RequestBody Owner owner)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateOwnerPassword method accessed by " + user.getEmail() + "***");

		Owner result;

		if (user.getAccountId() == owner.getAccountId()
				|| user.getRole() == Role.ADMIN) {

			result = accountService.saveOwnerPassword(owner);

		} else {
			logger.info("***updateOwnerPassword method failed. No priviledge for: " + user.getEmail() + " role: "
					+ user.getRole() + "***");
			throw new Exception("Only the customer themselves or an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/owners/" + result.getAccountId() + "/password")).body(result);

	}

	// Add Administratrator
	@ApiOperation(value = "Create an Administrator", response = Administrator.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Administrator"),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admins")
	public ResponseEntity<Administrator> saveAdministrator(Principal principal,
			@RequestBody Administrator administrator) throws Exception {
		logger.info("***saveAdministrator method accessed by " + principal.getName() + "***");
		Administrator result = null;

		Account user = AccountUtil.getAccount(principal.getName());

		if (administrator.getAccountId() == null
				|| administrator.getAccountId() == 0) {
			if (user.getRole() == Role.ADMIN) {

				result = accountService.saveAdministrator(administrator);
			}
		} else {
			throw new Exception("Please use the Put method to update an Administrator.");
		}

		return ResponseEntity.created(new URI("/administrator/" + result.getAccountId())).body(result);
	}

	@ApiOperation(value = "Update Administrator name", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Administrator name."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admins/{id}/name")
	public ResponseEntity<Administrator> updateAdministratorName(Principal principal, @RequestBody Administrator admin)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateAdministratorName method accessed by " + user.getEmail() + "***");

		Administrator result;

		result = accountService.saveAdministratorName(admin);

		return ResponseEntity.created(new URI("/administrators/" + result.getAccountId() + "/name")).body(result);

	}

	@ApiOperation(value = "Update Administrator email", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Administrator email."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admins/{id}/email")
	public ResponseEntity<Administrator> updateAdministratorEmail(Principal principal, @RequestBody Administrator admin)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateAdministratorEmail method accessed by " + user.getEmail() + "***");

		Administrator result;

		result = accountService.saveAdministratorEmail(admin);

		return ResponseEntity.created(new URI("/administrators/" + result.getAccountId() + "/email")).body(result);

	}

	@ApiOperation(value = "Update Administrator password", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Administrator password."),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admins/{id}/password")
	public ResponseEntity<Administrator> updateAdministratorPassword(Principal principal,
			@RequestBody Administrator admin)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		logger.info("***updateAdministratorPassword method accessed by " + user.getEmail() + "***");

		Administrator result;

		result = accountService.saveAdministratorPassword(admin);

		return ResponseEntity.created(new URI("/administrators/" + result.getAccountId() + "/password")).body(result);

	}

	// Add Administratrator
	@ApiOperation(value = "Create an Administrator", response = Administrator.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Administrator"),
			@ApiResponse(code = 400, message = "Invalid input"),
	})
	@PostMapping("/superadmin")
	public Administrator saveAdministrator() throws Exception {
		logger.info("***Creating SUPER ADMIN! ***");
		if (!Arrays.stream(this.environment.getActiveProfiles()).noneMatch(p -> p.equals("dev"))) {

			Administrator admin = new Administrator();

			admin.setAccountId(null);
			admin.setEmail("super@admin.com");
			admin.setName("SuperAdmin");
			admin.setPassword("password");
			admin.setRole(Role.ADMIN);

			return accountService.saveAdministrator(admin);
		}

		return null;
	}

	// Update Administrator
	@ApiOperation(value = "Update an Administrator", response = Administrator.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admins/{id}")
	public ResponseEntity<Administrator> updateAdministrator(Principal principal,
			@RequestBody Administrator administrator)
			throws Exception {
		logger.info("***updateAdministrator method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());
		Administrator result;

		if (user.getAccountId() == administrator.getAccountId()
				|| user.getRole() == Role.ADMIN) { // seems redundant here!

			result = accountService.saveAdministrator(administrator);

		} else {

			throw new Exception("Only an Administrator can update this account.");
		}
		return ResponseEntity.created(new URI("/administrators/" + result.getAccountId())).body(result);

	}

	// Delete a Customer
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
			accountService.deleteCustomer(user.getEmail(), id);
		}
	}

	// Delete Owner
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
				|| user.getRole() == Role.ADMIN) {
			accountService.deleteOwner(user.getName(), id);
		}
	}

	// Delete Admin
	@ApiOperation(value = "Delete an Administrator", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted Administrator account"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/admins/{id}")
	public void deleteAdministrator(Principal principal, @PathVariable Long id) throws NotFoundException {
		logger.info("***deleteAdministrator method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());

		if (user.getAccountId() == id
				|| user.getRole() == Role.ADMIN) { // seems redundant here!
			accountService.deleteAdministrator(user.getName(), id);
		}
	}
}
