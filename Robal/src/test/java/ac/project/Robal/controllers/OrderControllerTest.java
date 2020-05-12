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
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.models.Product;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.repositories.AdministratorRepository;
import ac.project.Robal.repositories.CustomerRepository;
import ac.project.Robal.repositories.OrderProductRepository;
import ac.project.Robal.repositories.OrderRepository;
import ac.project.Robal.repositories.OwnerRepository;
import ac.project.Robal.repositories.ProductRepository;
import ac.project.Robal.repositories.StoreProductRepository;
import ac.project.Robal.repositories.StoreRepository;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class OrderControllerTest extends Constants {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AdministratorRepository adminRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StoreProductRepository storeProductRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

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
	void createOrder() throws Exception {

		int databaseSizeBeforeCreate = orderRepository.findAll().size();

		this.mockMvc
				.perform(post("/orders/").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(getStore1())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.orderId").isNumber())
				.andExpect(jsonPath("$.invoiceNumber").value(INVOICE_NUMBER1))
				.andExpect(jsonPath("$.subTotal").value(SUB_TOTAL1))
				.andExpect(jsonPath("$.total").value(TOTAL1));
		/*
		 * .andExpect(jsonPath("$.purchaseDate").value(PURCHASE_DATE1)); //this might
		 * fail. need to pass a string not a date
		 */

		List<Order> orders = orderRepository.findAll();
		assertThat(orders.size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}

	@Test
	void updateOrder() throws Exception {

		saveOrder(getOrder1(), getOrderProduct1(), getStoreProduct1(), getProduct1());
//		orderRepository.save(getOrder1());

		adminRepository.save(getAdmin1());

		int databaseSizeBeforeCreate = orderRepository.findAll().size();

		Order updated = orderRepository.findById(getOrder1().getOrderId()).orElse(null);
		assertThat(updated).isNotNull();

		entityManager.detach(updated);
		updated.setInvoiceNumber(INVOICE_NUMBER1);
		updated.setSubTotal(SUB_TOTAL1);
		updated.setTotal(TOTAL1);
		updated.setPurchaseDate(PURCHASE_DATE1);

		this.mockMvc
				.perform(put("/orders/{id}", getOrder1().getOrderId())
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword()))
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(updated)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.orderId").isNumber())
				.andExpect(jsonPath("$.invoiceNumber").value(INVOICE_NUMBER1))
				.andExpect(jsonPath("$.subTotal").value(SUB_TOTAL1))
				.andExpect(jsonPath("$.total").value(TOTAL1));

		// It is also helpful to verify that the database has indeed changed
		Order databaseAccount = orderRepository.findById(getOrder1().getOrderId()).orElse(null);
		assertThat(databaseAccount).isNotNull();
		assertThat(databaseAccount.getInvoiceNumber()).isEqualTo(getOrder1().getInvoiceNumber());
		assertThat(databaseAccount.getSubTotal()).isEqualTo(getOrder1().getSubTotal());
		assertThat(databaseAccount.getTotal()).isEqualTo(getOrder1().getTotal());
		assertThat(databaseAccount.getPurchaseDate()).isEqualTo(getOrder1().getPurchaseDate());
	}

	@Test
	void findOrder() throws Exception {

		Order saved = saveOrder(getOrder1(), getOrderProduct1(), getStoreProduct1(), getProduct1());

//		Order saved = orderRepository.save(getOrder1());
		Administrator admin = adminRepository.save(getAdmin1());
		this.mockMvc.perform(get("/orders/{id}", saved.getOrderId())
				// Pass in the header
				.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.orderId").isNumber())
				.andExpect(jsonPath("$.invoiceNumber").value(INVOICE_NUMBER1))
				.andExpect(jsonPath("$.subTotal").value(SUB_TOTAL1))
				.andExpect(jsonPath("$.total").value(TOTAL1));
	}

	@Test
	void findStores() throws Exception {

		List<Order> orders = new ArrayList<>();

		saveOrder(getOrder1(), getOrderProduct1(), getStoreProduct1(), getProduct1());

//		orderRepository.save(getOrder1());
		orders.add(getOrder1());

		saveOrder(getOrder2(), getOrderProduct2(), getStoreProduct2(), getProduct2());

//		orderRepository.save(getOrder2());
		orders.add(getOrder2());

		saveOrder(getOrder3(), getOrderProduct3(), getStoreProduct3(), getProduct3());

//		orderRepository.save(getOrder3());
		orders.add(getOrder3());

		adminRepository.save(getAdmin1());

		MvcResult result = mockMvc
				.perform(get("/orders/")
						.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();

		// this uses a TypeReference to inform Jackson about the Lists's generic type
		List<Order> actual = mapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<List<Order>>() {
				});

		assertThat(actual.equals(orders));

	}

	@Test
	void deleteOrder() throws Exception {

		saveOrder(getOrder1(), getOrderProduct1(), getStoreProduct1(), getProduct1());

//		orderRepository.save(getOrder1());
		adminRepository.save(getAdmin1());
		int databaseSizeBeforeDelete = orderRepository.findAll().size();

		this.mockMvc.perform(delete("/orders/{id}", getOrder1().getOrderId())
				.headers(TestUtil.getAuthorizationBasic(getAdmin1().getEmail(), getAdmin1().getPassword())))
				.andExpect(status().isNoContent())
				.andReturn();

		// Validate the database is empty
		List<Order> orders = orderRepository.findAll();
		assertThat(orders.size()).isEqualTo(databaseSizeBeforeDelete - 1);

	}

	private Order saveOrder(Order order, OrderProduct orderProduct, StoreProduct storeProduct, Product product) {

		productRepository.save(product);
		storeProductRepository.save(storeProduct);
		orderProductRepository.save(orderProduct);
		orderRepository.save(order);

		return order;
	}

}
