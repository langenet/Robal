package ac.project.Robal.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ac.project.Robal.TestUtil;
import ac.project.Robal.enums.Role;
import ac.project.Robal.models.Customer;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.services.AccountService;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CustomerRepository customerRepository;
	
	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private Customer customer;
	
	private static final String NAME = "Andy Ta";
	private static final String EMAIL = "Andy@test.com";
	private static final String PASSWORD = "password";
	
	private static final Role ROLE = Role.CUSTOMER;
//	private static final List<Order> ORDERS = new ArrayList<Order>();
	private static final String BILLING_ADDRESS = "123 Main Street";
	private static final String PAYMENT_METHOD = "MasterCard";
//	private static final List<Order> ORDERS = Order.builder()
//													.invoiceNumber(1L)
//													.purchaseDate(LocalDate.now())
//													.orderProducts(orderProducts)
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		customer = Customer.builder()
				.name(NAME)
				.email(EMAIL)
				.password(PASSWORD)
				.role(ROLE)
				.billingAddress(BILLING_ADDRESS)
				.paymentMethod(PAYMENT_METHOD)
				.build();
		Mockito.when(bCryptPasswordEncoder.encode(any())).thenReturn(PASSWORD);
		Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
	}
	
	@Test
	void createCustomer() throws Exception {
		int databaseSizeBeforeCreate = customerRepository.findAll().size();
		
		this.mockMvc.perform(post("/customers/")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(this.customer)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountId").isNumber())
				.andExpect(jsonPath("$.name").value(NAME))
				.andExpect(jsonPath("$.email").value(EMAIL))
				.andExpect(jsonPath("$.role").value(ROLE.name())) /*TODO verify the json name for account type field*/
				.andExpect(jsonPath("$.billingAddress").value(BILLING_ADDRESS)) 
				.andExpect(jsonPath("$.paymentMethod").value(PAYMENT_METHOD)); 
		
		List<Customer> customers = customerRepository.findAll();
		assertThat(customers.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}
	
//	@Test
//	void findCustomer() throws Exception {
//		Customer saved = accountService.saveCustomer(customer);
//	}
	
}
