package ac.project.Robal.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.Owner;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OwnerRepository;
import javassist.NotFoundException;

@Service
public class AccountService {

	Logger logger = LoggerFactory.getLogger(AccountService.class);

	private CustomerRepository customerRepository;
	private OwnerRepository ownerRepository;
	private AdministratorRepository administratorRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public AccountService(CustomerRepository customerRepository, OwnerRepository ownerRepository,
			AdministratorRepository administratorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.customerRepository = customerRepository;
		this.ownerRepository = ownerRepository;
		this.administratorRepository = administratorRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;

	}

	public Customer findCustomer(Long id) throws NotFoundException {
		return customerRepository.findById(id).orElseThrow(accountNotFound("Customer"));
	}

	public Customer findCustomerByEmail(String email) throws NotFoundException {
		return customerRepository.findByEmail(email).orElseThrow(accountNotFound("Customer"));
	}

	public List<Customer> listCustomers() {
		return new ArrayList<Customer>(customerRepository.findAll());
	}

	public List<Order> listCustomerOrders(Long customerId) throws NotFoundException {
		return customerRepository.findById(customerId).orElseThrow(accountNotFound("Customer")).getOrders();
	}

	public Owner findOwner(Long id) throws NotFoundException {

		return ownerRepository.findById(id).orElseThrow(accountNotFound("Owner"));
	}

	public Owner findOwnerByEmail(String email) throws NotFoundException {
		logger.info("***Repo:Query owner account  for email: " + email + "***");
		return ownerRepository.findByEmail(email).orElseThrow(accountNotFound("Owner"));
	}

	public List<Owner> listOwners() {
		return new ArrayList<Owner>(ownerRepository.findAll());
	}

	public Administrator findAdministrator(Long id) throws NotFoundException {
		logger.info("***Repo:Query administrator account attemped for id: " + id + "***");
		return administratorRepository.findById(id).orElseThrow(accountNotFound("Administrator"));
	}

	public List<Administrator> listAdministrators() {
		return new ArrayList<Administrator>(administratorRepository.findAll());
	}


	public Customer saveCustomer(Customer customer) throws Exception {
		if (customer.getName() == null || customer.getEmail() == null || customer.getPassword() == null) {

			// TODO jUnit test saveCustomer null values
			throw new ClientException("Cannot create or update customer without Name, Email or Password.");
		}

		if ((customer.getAccountId() == null || customer.getAccountId() == 0)) {

			if (emailAlreadyExists(customer.getEmail())) {
				logger.info("***saveCustomer method account already exists for " + customer.getName() + " with email "
						+ customer.getEmail() + "***");
				throw new Exception("Customer account already exist.");
			}
			customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
			logger.info("***Repo:saveCustomer(new) attemped for: " + customer.getEmail() + "***");
			return customerRepository.save(customer);
		} else {
			// TODO jUnit test update customer
			logger.info("***Repo:updateCustomer attemped for: " + customer.getEmail() + "***");
			Customer dbCustomer = customerRepository.findById(customer.getAccountId())
					.orElseThrow(accountNotFound("Customer"));

			dbCustomer.setName(customer.getName());
			dbCustomer.setBillingAddress(customer.getBillingAddress());
			dbCustomer.setEmail(customer.getEmail());
			dbCustomer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
			dbCustomer.setRole(customer.getRole());
			dbCustomer.setPaymentMethod(customer.getPaymentMethod());

			// TODO test that this does not wipe out any orders or duplicate them
			// TODO validation that order is complete
			for (Order order : customer.getOrders()) {
				if (!dbCustomer.getOrders().contains(order)) {
					dbCustomer.getOrders().add(order);
				}
			}
			return customerRepository.save(dbCustomer);
		}
	}

	public Customer saveCustomerName(Customer customer) throws Exception, NotFoundException {

		String name = customer.getName();
		customer = customerRepository.findById(customer.getAccountId())
				.orElse(null)/* .orElseThrow(accountNotFound("Customer")) */;
		if (customer != null) {
			customer.setName(name);

		}
		logger.info("***Repo:update customer name attemped for: " + customer.getEmail() + "***");
		return saveCustomer(customer);

	}

	public Customer saveCustomerEmail(Customer customer) throws Exception, NotFoundException {

		String email = customer.getEmail();

		if (emailAlreadyExists(email)) {
			throw new Exception("Email already taken.");
		}

		customer = customerRepository.findById(customer.getAccountId()).orElseThrow(accountNotFound("Customer"));
		customer.setEmail(email);
		logger.info("***Repo:update customer email attemped for: " + customer.getEmail() + "***");
		return saveCustomer(customer);

	}

	public Customer saveCustomerPassword(Customer customer) throws Exception, NotFoundException {

		String password = customer.getPassword();
		customer = customerRepository.findById(customer.getAccountId()).orElseThrow(accountNotFound("Customer"));
		customer.setPassword(bCryptPasswordEncoder.encode(password));
		logger.info("***Repo:update customer password attemped for: " + customer.getEmail() + "***");
		return saveCustomer(customer);

	}

	public Customer saveCustomerBillingAddress(Customer customer) throws Exception, NotFoundException {

		String billingAddress = customer.getBillingAddress();
		customer = customerRepository.findById(customer.getAccountId()).orElseThrow(accountNotFound("Customer"));
		customer.setName(billingAddress);

		return saveCustomer(customer);

	}

	public Customer saveCustomerPaymentMethod(Customer customer) throws Exception, NotFoundException {

		String paymentMethod = customer.getPaymentMethod();
		customer = customerRepository.findById(customer.getAccountId()).orElseThrow(accountNotFound("Customer"));
		customer.setName(paymentMethod);

		return saveCustomer(customer);

	}

	// Owner
	public Owner saveOwner(Owner owner) throws Exception {
		if (owner.getName() == null || owner.getEmail() == null || owner.getPassword() == null) {

			// TODO jUnit test saveowner null values
			throw new ClientException("Cannot create or update owner without Name, Email or Password.");
		}

		if ((owner.getAccountId() == null || owner.getAccountId() == 0)) {
			if (emailAlreadyExists(owner.getEmail())) {
				logger.info("***saveCustomer method account already exists for " + owner.getName() + " with email "
						+ owner.getEmail() + "***");
				throw new Exception("Customer account already exist.");
			}
			owner.setPassword(bCryptPasswordEncoder.encode(owner.getPassword()));
			logger.info("***Repo:saveOwner(new) attemped for: " + owner.getEmail() + "***");
			return ownerRepository.save(owner);
		} else {
			// TODO jUnit test update owner
			Owner dbOwner = ownerRepository.findById(owner.getAccountId()).orElseThrow(accountNotFound("Owner"));

			dbOwner.setName(owner.getName());
			dbOwner.setEmail(owner.getEmail());
			dbOwner.setPassword(bCryptPasswordEncoder.encode(owner.getPassword()));
			dbOwner.setRole(owner.getRole());

			// TODO test that this does not wipe out any orders or duplicate them
			// TODO validation that order is complete
			logger.info("***Repo:saveOwner(update) attemped for: " + owner.getEmail() + "***");
			return ownerRepository.save(dbOwner);
		}
	}

	public Owner saveOwnerName(Owner owner) throws Exception, NotFoundException {

		String name = owner.getName();
		owner = ownerRepository.findById(owner.getAccountId()).orElseThrow(accountNotFound("Owner"));
		owner.setName(name);

		return saveOwner(owner);

	}

	public Owner saveOwnerEmail(Owner owner) throws Exception, NotFoundException {

		String email = owner.getEmail();
		owner = ownerRepository.findById(owner.getAccountId()).orElseThrow(accountNotFound("Owner"));
		owner.setName(email);

		return saveOwner(owner);

	}

	public Owner saveOwnerPassword(Owner owner) throws Exception, NotFoundException {

		String password = owner.getPassword();
		owner = ownerRepository.findById(owner.getAccountId()).orElseThrow(accountNotFound("Owner"));
		owner.setPassword(bCryptPasswordEncoder.encode(password));

		return saveOwner(owner);

	}

	// Administrator
	public Administrator saveAdministrator(Administrator administrator) throws Exception {

		if (administrator.getName() == null || administrator.getEmail() == null
				|| administrator.getPassword() == null) {
			// TODO Logging
			// TODO jUnit test saveowner null values
			throw new ClientException("Cannot create or update Administrator without Name, Email or Password.");
		}
		if ((administrator.getAccountId() == null || administrator.getAccountId() == 0)) {

			if (emailAlreadyExists(administrator.getEmail())) {
				logger.info(
						"***saveCustomer method account already exists for " + administrator.getName() + " with email "
								+ administrator.getEmail() + "***");
				throw new Exception("Customer account already exist.");
			}

			administrator.setPassword(bCryptPasswordEncoder.encode(administrator.getPassword()));
			return administratorRepository.save(administrator);
		} else {
			// TODO jUnit test update owner
			Administrator dbAdmin = administratorRepository.findById(administrator.getAccountId())
					.orElseThrow(accountNotFound("Administrator"));

			dbAdmin.setName(administrator.getName());
			dbAdmin.setEmail(administrator.getEmail());
			dbAdmin.setPassword(bCryptPasswordEncoder.encode(administrator.getPassword()));
			dbAdmin.setRole(administrator.getRole());

			// TODO test that this does not wipe out any orders or duplicate them
			// TODO validation that order is complete

			return administratorRepository.save(dbAdmin);
		}
	}

	public Administrator saveAdministratorName(Administrator admin) throws Exception, NotFoundException {

		String name = admin.getName();
		admin = administratorRepository.findById(admin.getAccountId()).orElseThrow(accountNotFound("Administrator"));
		admin.setName(name);

		return saveAdministrator(admin);

	}

	public Administrator saveAdministratorEmail(Administrator admin) throws Exception, NotFoundException {

		String email = admin.getEmail();
		admin = administratorRepository.findById(admin.getAccountId()).orElseThrow(accountNotFound("Administrator"));
		admin.setName(email);

		return saveAdministrator(admin);

	}

	public Administrator saveAdministratorPassword(Administrator admin) throws Exception, NotFoundException {

		String password = admin.getPassword();
		admin = administratorRepository.findById(admin.getAccountId()).orElseThrow(accountNotFound("Administrator"));
		admin.setPassword(bCryptPasswordEncoder.encode(password));

		return saveAdministrator(admin);

	}

	public void deleteCustomer(String email, Long id) throws NotFoundException {
		logger.info("***Repo:deleteCustomer attemped for: " + email + " id of " + id + "***");
		customerRepository.delete(customerRepository.findById(id).orElseThrow(accountNotFound("Customer")));
	}

	public void deleteOwner(String email, Long id) throws NotFoundException {
		logger.info("***Repo:deleteOwner attemped for: " + email + " id of " + id + "***");

		ownerRepository.delete(ownerRepository.findById(id).orElseThrow(accountNotFound("Owner")));
	}

	public void deleteAdministrator(String email, Long id) throws NotFoundException {
		logger.info("***Repo:deleteAdministrator attemped for: " + email + " id of " + id + "***");

		administratorRepository
				.delete(administratorRepository.findById(id).orElseThrow(accountNotFound("Administrator")));
	}

	private boolean emailAlreadyExists(String email) {

		return customerRepository.findByEmail(email).isPresent() ? true
				: ownerRepository.findByEmail(email).isPresent() ? true
						: administratorRepository.findByEmail(email).isPresent() ? true : false;
	}

	private Supplier<NotFoundException> accountNotFound(String accountType) {
		return () -> {
			logger.info("***Lookup failed for: " + accountType + "***");
			return new NotFoundException("The " + accountType + " account was not found.");
		};
	}
}