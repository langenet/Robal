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

import java.util.ArrayList;

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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ac.project.Robal.TestUtil;
import ac.project.Robal.enums.Constants;
import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Store;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.OwnerRepository;
import ac.project.Robal.repositories.ProductRepository;
import ac.project.Robal.repositories.StoreRepository;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class StoreControllerTest extends Constants {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private AdministratorRepository adminRepository;

	@Autowired
	private StoreRepository storeRepository;

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
	void createStore() throws Exception {

		int databaseSizeBeforeCreate = storeRepository.findAll().size();

		this.mockMvc
				.perform(post("/stores/").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(getStore1())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.storeId").isNumber())
				.andExpect(jsonPath("$.name").value(STORE_NAME1))
				.andExpect(jsonPath("$.address").value(STORE_ADDRESS1));

		List<Store> stores = storeRepository.findAll();
		assertThat(stores.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}

	@Test
	void updateStore() throws Exception {

		storeRepository.save(getStore1());
		adminRepository.save(getAdmin1());

		int databaseSizeBeforeCreate = storeRepository.findAll().size();

		Store updated = storeRepository.findById(getStore1().getStoreId()).orElse(null);
		assertThat(updated).isNotNull();

		entityManager.detach(updated);
		updated.setName(STORE_NAME2);
		updated.setAddress(STORE_ADDRESS2);

		this.mockMvc
				.perform(put("/stores/{id}", getStore1().getStoreId())
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword()))
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updated)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.storeId").isNumber())
				.andExpect(jsonPath("$.name").value(STORE_NAME2))
				.andExpect(jsonPath("$.address").value(STORE_ADDRESS2));

		// It is also helpful to verify that the database has indeed changed
		Store databaseAccount = storeRepository.findById(getStore1().getStoreId()).orElse(null);
		assertThat(databaseAccount).isNotNull();
		assertThat(databaseAccount.getName()).isEqualTo(getStore1().getName());
		assertThat(databaseAccount.getAddress()).isEqualTo(getStore1().getAddress());
	}

	@Test
	void findStore() throws Exception {

		Store saved = storeRepository.save(getStore1());
		Administrator admin = adminRepository.save(getAdmin1());
		this.mockMvc.perform(get("/stores/{id}", saved.getStoreId())
				// Pass in the header
				.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.storeId").isNumber())
				.andExpect(jsonPath("$.name").value(STORE_NAME2))
				.andExpect(jsonPath("$.address").value(STORE_ADDRESS2));
	}

	@Test
	void findStores() throws Exception {

		List<Store> stores = new ArrayList<>();

		storeRepository.save(getStore1());
		stores.add(getStore1());

		storeRepository.save(getStore2());
		stores.add(getStore2());

		storeRepository.save(getStore3());
		stores.add(getStore3());

		adminRepository.save(getAdmin1());

		MvcResult result = mockMvc
				.perform(get("/stores/")
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();

		// this uses a TypeReference to inform Jackson about the Lists's generic type
		List<Store> actual = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Store>>() {
				});

		assertThat(actual.equals(stores));

	}

	@Test
	void deleteOwner() throws Exception {

		storeRepository.save(getStore1());
		adminRepository.save(getAdmin1());
		int databaseSizeBeforeDelete = storeRepository.findAll().size();

		this.mockMvc.perform(delete("/stores/{id}", getStore1().getStoreId())
				.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk())
				.andReturn();

		// Validate the database is empty
		List<Store> stores = storeRepository.findAll();
		assertThat(stores.size()).isEqualTo(databaseSizeBeforeDelete - 1);

	}

}
