package ac.project.Robal.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ac.project.Robal.TestUtil;
import ac.project.Robal.enums.AccountType;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.services.AccountService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

	private Customer customer;
	
	private static final String NAME = "Andy Ta";
	private static final String EMAIL = "Andy@test.com";
	private static final AccountType ACCOUNT_TYPE = AccountType.CUSTOMER;
//	private static final List<Order> ORDERS = new ArrayList<Order>();
	private static final String BILLING_ADDRESS = "123 Main Street";
	private static final String PAYMENT_METHOD = "MasterCard";
	

	@Test
	void createCustomer() throws Exception {
		int databaseSizeBeforeCreate = customerRepository.findAll().size();
		
		this.mockMvc.perform(post("/customers/")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(this.customer)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value(NAME))
				.andExpect(jsonPath("$.email").value(EMAIL))
				.andExpect(jsonPath("$.accountType").value(ACCOUNT_TYPE)) /*TODO verify the json name for account type field*/
				.andExpect(jsonPath("$.billingAddress").value(BILLING_ADDRESS)) 
				.andExpect(jsonPath("$.billingAddress").value(PAYMENT_METHOD)); 
		
		List<Customer> customers = customerRepository.findAll();
		assertThat(customers.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}
	
	@Test
	void findCustomer() throws Exception {
		Customer saved = accountService.saveCustomer(customer);
	}
	
}
