package ac.project.Robal.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/*
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import ac.project.Robal.RobalApplication;
import ac.project.Robal.enums.Constants;
import ac.project.Robal.models.Owner;
import ac.project.Robal.repositories.OwnerRepository;
import ac.project.Robal.repositories.StoreRepository;
import ac.project.Robal.services.AccountService;

//@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RobalApplication.class)
@SpringBootTest
//@Transactional
@AutoConfigureMockMvc
public class OwnerControllerTest extends Constants {
//
//	@Autowired
//	private WebApplicationContext context;

//	@Autowired
//	private TestRestTemplate template;

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


	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

//		mockMvc = MockMvcBuilders
//				.webAppContextSetup(context)
//				.apply(springSecurity())
//				.build();
//
//		owner = Owner.builder().name(NAME).email(EMAIL).password(PASSWORD).role(ROLE).build();
//
//		STORE_PRODUCTS.add(StoreProduct.builder().inventory(INVENTORY).price(PRICE).product(PRODUCT).build());
//
//		STORES.add(Store.builder().address(STORE_ADDRESS).name(STORE_NAME).owner(owner).storeProducts(STORE_PRODUCTS)
//				.build());

		Mockito.when(bCryptPasswordEncoder.encode(any())).thenReturn(PASSWORD);
		Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
	}

	@WithMockUser(roles = "ADMIN")
	@Test
	void createOwner() throws Exception {

		int databaseSizeBeforeCreate = ownerRepository.findAll().size();

//		this.mockMvc
//				.perform(post("/owners/").contentType(TestUtil.APPLICATION_JSON_UTF8)
//						.content(TestUtil.convertObjectToJsonBytes(this.owner)))
//				.andExpect(status().isCreated())
//				.andExpect(jsonPath("$.accountId").isNumber())
//				.andExpect(jsonPath("$.name").value(NAME))
//				.andExpect(jsonPath("$.email").value(EMAIL))
//				.andExpect(jsonPath("$.role").value(ROLE.name()));

		List<Owner> owners = ownerRepository.findAll();
		assertThat(owners.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}

//	@Test
//	void findCustomer() throws Exception {
//		Customer saved = accountService.saveCustomer(customer);
//	}

}
