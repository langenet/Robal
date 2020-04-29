package ac.project.Robal.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ac.project.Robal.TestUtil;
import ac.project.Robal.enums.Role;
import ac.project.Robal.models.Owner;
import ac.project.Robal.models.Product;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.repositories.OwnerRepository;
import ac.project.Robal.repositories.StoreRepository;
import ac.project.Robal.services.AccountService;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class OwnerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AccountService accountService;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private StoreRepository storeRepository;

	@MockBean
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private Owner owner;

	private static final String NAME = "Andy Ta";
	private static final String EMAIL = "Andy@test.com";
	private static final String PASSWORD = "password";

	private static final Role ROLE = Role.OWNER;
	private static final List<Store> STORES = new ArrayList<>();

	private static final String STORE_ADDRESS = "123 Store Stree";
	private static final String STORE_NAME = "Walmart";

	private static final List<StoreProduct> STORE_PRODUCTS = new ArrayList<>();

	private static final int INVENTORY = 1;
	private static final double PRICE = 5.25;

	private static final String DESCRIPTION = "Toilet Paper";
	private static final String PRODUCT_NAME = "Charmen";

	private static final Long SKU = 123L;
	private static final Long PRODUCT_ID = 1L;

	private static final Product PRODUCT = Product.builder().description(DESCRIPTION).name(PRODUCT_NAME).sku(SKU)
			.productId(PRODUCT_ID).build();

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		owner = Owner.builder().name(NAME).email(EMAIL).password(PASSWORD).role(ROLE).build();

		STORE_PRODUCTS.add(StoreProduct.builder().inventory(INVENTORY).price(PRICE).product(PRODUCT).build());

		STORES.add(Store.builder().address(STORE_ADDRESS).name(STORE_NAME).owner(owner).storeProducts(STORE_PRODUCTS)
				.build());

		Mockito.when(bCryptPasswordEncoder.encode(any())).thenReturn(PASSWORD);
		Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
	}

	@Test
	void createOwner() throws Exception {
		int databaseSizeBeforeCreate = ownerRepository.findAll().size();

		this.mockMvc
				.perform(post("/owner/").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(this.owner)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.accountId").isNumber())
				.andExpect(jsonPath("$.name").value(NAME)).andExpect(jsonPath("$.email").value(EMAIL)).andExpect(
						jsonPath("$.role").value(ROLE.name())); /* TODO verify the json name for account type field */

		List<Owner> owners = ownerRepository.findAll();
		assertThat(owners.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}

//	@Test
//	void findCustomer() throws Exception {
//		Customer saved = accountService.saveCustomer(customer);
//	}

}
