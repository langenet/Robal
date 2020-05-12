package ac.project.Robal.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ac.project.Robal.TestUtil;
import ac.project.Robal.enums.Constants;
import ac.project.Robal.models.Account;
import ac.project.Robal.models.Owner;
import ac.project.Robal.repositories.OwnerRepository;
import ac.project.Robal.repositories.StoreRepository;
import ac.project.Robal.services.AccountService;

@SpringBootTest
@AutoConfigureMockMvc
public class OwnerControllerTest extends Constants {

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

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		setupTests();

		Mockito.when(bCryptPasswordEncoder.encode(any())).thenReturn(PASSWORD);
		Mockito.when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);
	}

	@Test
	void createOwner() throws Exception {

		int databaseSizeBeforeCreate = ownerRepository.findAll().size();

		this.mockMvc
				.perform(post("/owners/").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(getOwner1())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountId").isNumber())
				.andExpect(jsonPath("$.name").value(NAME1))
				.andExpect(jsonPath("$.email").value(EMAIL_OWNER1))
				.andExpect(jsonPath("$.role").value(OWNER_ROLE.name()));

		List<Owner> owners = ownerRepository.findAll();
		assertThat(owners.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}

	@Test
	void updateOwner() throws Exception {

		accountService.saveOwner(getOwner1());
		accountService.saveAdministrator(getAdmin1());

		int databaseSizeBeforeCreate = ownerRepository.findAll().size();

		Account updated = ownerRepository.findById(getOwner1().getAccountId()).orElse(null);
		assertThat(updated).isNotNull();

		entityManager.detach(updated);
		updated.setName(NAME2);
		updated.setEmail(EMAIL_OWNER2);

		this.mockMvc
				.perform(post("/owners/{id}", getOwner1().getAccountId())
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword()))
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(getOwner1())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accountId").isNumber())
				.andExpect(jsonPath("$.name").value(NAME1))
				.andExpect(jsonPath("$.email").value(EMAIL_OWNER1))
				.andExpect(jsonPath("$.role").value(OWNER_ROLE.name()));

		List<Owner> owners = ownerRepository.findAll();
		assertThat(owners.size()).isEqualTo(databaseSizeBeforeCreate + 1);

		// It is also helpful to verify that the database has indeed changed
		Account databaseAccount = ownerRepository.findById(getOwner1().getAccountId()).orElse(null);
		assertThat(databaseAccount).isNotNull();
		assertThat(databaseAccount.getName()).isEqualTo(getOwner1().getName());
		assertThat(databaseAccount.getEmail()).isEqualTo(getOwner1().getEmail());
		assertThat(databaseAccount.getRole()).isEqualTo(getOwner1().getRole());
	}

	@Test
	void findOwner() throws Exception {

		Account saved = accountService.saveOwner(getOwner1());
		Account admin = accountService.saveAdministrator(getAdmin1());
		this.mockMvc.perform(get("/owners/{id}", saved.getAccountId())
				// Pass in the header
				.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.accountId").isNumber())
				.andExpect(jsonPath("$.name").value(NAME1))
				.andExpect(jsonPath("$.email").value(EMAIL_OWNER1))
				.andExpect(jsonPath("$.role").value(OWNER_ROLE.name()));
	}

	@Test
	void findOwners() throws Exception {

		List<Owner> owners = new ArrayList<>();

		accountService.saveOwner(getOwner1());
		owners.add(getOwner1());

		accountService.saveOwner(getOwner2());
		owners.add(getOwner2());

		accountService.saveOwner(getOwner3());
		owners.add(getOwner3());

		accountService.saveAdministrator(getAdmin1());

		MvcResult result = mockMvc
				.perform(get("/owners/")
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();

		// this uses a TypeReference to inform Jackson about the Lists's generic type
		List<Owner> actual = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Owner>>() {
				});

		assertThat(actual.equals(owners));

	}

	@Test
	void deleteOwner() throws Exception {

		accountService.saveOwner(getOwner1());
		accountService.saveAdministrator(getAdmin1());
		int databaseSizeBeforeDelete = ownerRepository.findAll().size();

		this.mockMvc.perform(delete("/owners/{id}", getOwner1().getAccountId())
				.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk())
				.andReturn();

		// Validate the database is empty
		List<Owner> accounts = ownerRepository.findAll();
		assertThat(accounts.size()).isEqualTo(databaseSizeBeforeDelete - 1);

	}

}
