package ac.project.Robal.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * 
 * import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 */

import java.util.ArrayList;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ac.project.Robal.TestUtil;
import ac.project.Robal.enums.Constants;
import ac.project.Robal.models.Customer;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.services.AccountService;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CustomerControllerTest extends Constants {

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
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(bCryptPasswordEncoder.encode(any())).thenReturn(PASSWORD);
		Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

	}
	
	@Test
	void createCustomer() throws Exception {
		int databaseSizeBeforeCreate = customerRepository.findAll().size();

		this.mockMvc.perform(post("/customers/")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(CUSTOMER1)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountId").isNumber())
				.andExpect(jsonPath("$.name").value(NAME1))
				.andExpect(jsonPath("$.email").value(EMAIL_CUSTOMER1))
				.andExpect(jsonPath("$.role").value(CUSTOMER_ROLE.name()))
				.andExpect(jsonPath("$.billingAddress").value(BILLING_ADDRESS))
				.andExpect(jsonPath("$.paymentMethod").value(PAYMENT_METHOD));
//		
		List<Customer> customers = customerRepository.findAll();
		assertThat(customers.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}
	
	@Test
	void findCustomer() throws Exception {
		int databaseSizeBeforeCreate = customerRepository.findAll().size();

		// Save customer1

		// GET on customers/{id} pass in CUSTOMER1.getAccountId()
		this.mockMvc.perform(post("/customers/")
				// Pass in the header

				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(CUSTOMER1)))

				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountId").isNumber())
				.andExpect(jsonPath("$.name").value(NAME1))
				.andExpect(jsonPath("$.email").value(EMAIL_CUSTOMER1))
				.andExpect(jsonPath("$.role").value(CUSTOMER_ROLE.name()))
				.andExpect(jsonPath("$.billingAddress").value(BILLING_ADDRESS))
				.andExpect(jsonPath("$.paymentMethod").value(PAYMENT_METHOD));
//		
	}

	@Test
	void findCustomers() throws Exception {

		List<Customer> customers = new ArrayList<>();

		accountService.saveCustomer(CUSTOMER1);
		customers.add(CUSTOMER1);

		accountService.saveCustomer(CUSTOMER2);
		customers.add(CUSTOMER2);

		accountService.saveCustomer(CUSTOMER3);
		customers.add(CUSTOMER3);


		accountService.saveAdministrator(ADMIN1);

		MvcResult result = mockMvc.perform(get("/customers/")
				.headers(TestUtil.getAuthorizationBasic(ADMIN1.getEmail(),
						ADMIN1.getPassword())))

				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();

		// this uses a TypeReference to inform Jackson about the Lists's generic type
		List<Customer> actual = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Customer>>() {
				});


		assertThat(actual.equals(customers));

	}
	
}
