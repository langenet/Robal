package ac.project.Robal.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import ac.project.Robal.models.Account;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Product;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.ProductRepository;
import ac.project.Robal.services.AccountService;
import ac.project.Robal.services.ProductService;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductControllerTest extends Constants {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProductRepository productRepository;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		setupTests();	
		
		Mockito.when(bCryptPasswordEncoder.encode(any())).thenReturn(PASSWORD);
		Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

	}

	@Test
	void createProduct() throws Exception {
		Administrator admin = accountService.saveAdministrator(getAdmin1());
		int databaseSizeBeforeCreate = productRepository.findAll().size();


		this.mockMvc
				.perform(post("/products/").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword()))
						.content(TestUtil.convertObjectToJsonBytes(getProduct1())))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.productId").isNumber())
				.andExpect(jsonPath("$.description").value(PRO_DESC1))
				.andExpect(jsonPath("$.name").value(PRO_NAME1))
				.andExpect(jsonPath("$.sku").value(PRO_SKU1));
		
		List<Product> products = productRepository.findAll();
		assertThat(products.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}

	@Test
	void findProduct() throws Exception {

		Administrator admin = accountService.saveAdministrator(getAdmin1());
		productRepository.save(getProduct1());
		
		this.mockMvc.perform(get("/products/{id}", getProduct1().getProductId())
				// Pass in the header
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description").value(PRO_DESC1))
				.andExpect(jsonPath("$.name").value(PRO_NAME1))
				.andExpect(jsonPath("$.sku").value(PRO_SKU1));
		
	}
	
	@Test
	void DeleteProduct() throws Exception {
		
		Account admin = accountService.saveAdministrator(getAdmin1());
		productRepository.save(getProduct1());
		int databaseSizeBeforeDelete = productRepository.findAll().size();
		
	
		this.mockMvc.perform(delete("/products/{id}", getProduct1().getProductId())
				// Pass in the header
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk())				
				.andReturn();
				
				// Validate the database is empty
				List<Product> product = productRepository.findAll();
				assertThat(product.size()).isEqualTo(databaseSizeBeforeDelete - 1);
		
	}

	
	
	@Test
	void findProducts() throws Exception {

		List<Product> products = new ArrayList<>();

		productRepository.save(getProduct1());
		products.add(getProduct1());

		productRepository.save(getProduct2());
		products.add(getProduct2());

		productRepository.save(getProduct3());
		products.add(getProduct3());

		accountService.saveAdministrator(getAdmin1());

		MvcResult result = mockMvc
				.perform(get("/products/")
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
						.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

		ObjectMapper mapper = new ObjectMapper();

		// this uses a TypeReference to inform Jackson about the Lists's generic type
		List<Product> actual = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Product>>() {
				});

		assertThat(actual.equals(products));

	}	
}
