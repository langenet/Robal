package ac.project.Robal.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Order;
import ac.project.Robal.models.OrderProduct;
import ac.project.Robal.models.Owner;
import ac.project.Robal.models.Product;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Constants {

	protected static final String NAME1 = "Andy Ta";
	protected static final String NAME2 = "Robert Lange";
	protected static final String NAME3 = "Alex Riccio";
	protected static final String UPDATED_NAME = "superman";

	protected static final String EMAIL_CUSTOMER1 = "andy@customer.com";
	protected static final String EMAIL_CUSTOMER2 = "robert@customer.com";
	protected static final String EMAIL_CUSTOMER3 = "alex@customer.com";

	protected static final String EMAIL_OWNER1 = "andy@owner.com";
	protected static final String EMAIL_OWNER2 = "robert@owner.com";
	protected static final String EMAIL_OWNER3 = "alex@owner.com";

	protected static final String EMAIL_ADMIN1 = "andy@admin.com";
	protected static final String EMAIL_ADMIN2 = "robert@admin.com";
	protected static final String EMAIL_ADMIN3 = "alex@admin.com";

	protected static final String PASSWORD = "password";

	protected static final Role CUSTOMER_ROLE = Role.CUSTOMER;
	protected static final Role OWNER_ROLE = Role.OWNER;
	protected static final Role ADMIN_ROLE = Role.ADMIN;

//	protected static final List<Order> ORDERS = new ArrayList<Order>();
	protected static final String BILLING_ADDRESS = "123 Main Street";
	protected static final String PAYMENT_METHOD = "MasterCard";

	private Customer customer1;
	private Customer customer2;
	private Customer customer3;

	private Owner owner1;
	private Owner owner2;
	private Owner owner3;

	private Administrator admin1;
	private Administrator admin2;
	private Administrator admin3;

	protected static final String PRO_DESC1 = "Toilet Paper";
	protected static final String PRO_DESC2 = "Tooth Paste";
	protected static final String PRO_DESC3 = "Lysol Cleaner";

	protected static final String PRO_NAME1 = "Charman";
	protected static final String PRO_NAME2 = "Colgate";
	protected static final String PRO_NAME3 = "Trump Juice";

	protected static final long PRO_SKU1 = 123L;
	protected static final long PRO_SKU2 = 456L;
	protected static final long PRO_SKU3 = 789L;

	private Product product1;
	private Product product2;
	private Product product3;

	protected static final String STORE_ADDRESS1 = "123 Store Street";
	protected static final String STORE_ADDRESS2 = "456 Store Ave";
	protected static final String STORE_ADDRESS3 = "789 Store Road";
	protected static final String STORE_NAME1 = "Walmart";
	protected static final String STORE_NAME2 = "Shoppers";
	protected static final String STORE_NAME3 = "Loblaws";

	protected static final int INVENTORY1 = 1;
	protected static final int INVENTORY2 = 2;
	protected static final int INVENTORY3 = 3;

	protected static final double PRICE1 = 1.25;
	protected static final double PRICE2 = 2.50;
	protected static final double PRICE3 = 3.75;
	protected static final double GST = 1.13;

	private Store store1;
	private Store store2;
	private Store store3;

	private List<Store> stores1 = new ArrayList<>();
	private List<Store> stores2 = new ArrayList<>();
	private List<Store> stores3 = new ArrayList<>();

	private StoreProduct storeProduct1;
	private StoreProduct storeProduct2;
	private StoreProduct storeProduct3;

	private List<StoreProduct> storeProducts1 = new ArrayList<>();
	private List<StoreProduct> storeProducts2 = new ArrayList<>();
	private List<StoreProduct> storeProducts3 = new ArrayList<>();

	protected static final Long INVOICE_NUMBER1 = 234567L;
	protected static final Long INVOICE_NUMBER2 = 456789L;
	protected static final Long INVOICE_NUMBER3 = 6789L;

	protected static final LocalDate PURCHASE_DATE1 = LocalDate.of(2020, 05, 11);
	protected static final LocalDate PURCHASE_DATE2 = LocalDate.of(2017, 01, 03);
	protected static final LocalDate PURCHASE_DATE3 = LocalDate.of(2019, 12, 31);

	protected static final int QUANTITY1 = 1;
	protected static final int QUANTITY2 = 1;
	protected static final int QUANTITY3 = 1;

	protected static final Double SUB_TOTAL1 = BigDecimal.valueOf(PRICE1).setScale(2, RoundingMode.HALF_UP)
			.doubleValue();
	protected static final Double SUB_TOTAL2 = BigDecimal.valueOf(PRICE2).setScale(2, RoundingMode.HALF_UP)
			.doubleValue();
	protected static final Double SUB_TOTAL3 = BigDecimal.valueOf(PRICE3).setScale(2, RoundingMode.HALF_UP)
			.doubleValue();

	protected static final Double TOTAL1 = BigDecimal.valueOf(SUB_TOTAL1 * GST).setScale(2, RoundingMode.HALF_UP)
			.doubleValue();
	protected static final Double TOTAL2 = BigDecimal.valueOf(SUB_TOTAL2 * GST).setScale(2, RoundingMode.HALF_UP)
			.doubleValue();
	protected static final Double TOTAL3 = BigDecimal.valueOf(SUB_TOTAL3 * GST).setScale(2, RoundingMode.HALF_UP)
			.doubleValue();

	private Order order1;
	private Order order2;
	private Order order3;

	private List<Order> orders1 = new ArrayList<>();
	private List<Order> orders2 = new ArrayList<>();
	private List<Order> orders3 = new ArrayList<>();

	private OrderProduct orderProduct1;
	private OrderProduct orderProduct2;
	private OrderProduct orderProduct3;

	private List<OrderProduct> orderProducts1 = new ArrayList<>();
	private List<OrderProduct> orderProducts2 = new ArrayList<>();
	private List<OrderProduct> orderProducts3 = new ArrayList<>();

	public void setupTests() {

		customer1 = Customer.builder()
				.billingAddress(BILLING_ADDRESS)
				.email(EMAIL_CUSTOMER1)
				.name(NAME1)
				.password(PASSWORD)
				.paymentMethod(PAYMENT_METHOD)
				.role(CUSTOMER_ROLE)
//				.orders(orders1)
				.build();

		customer2 = Customer.builder()
				.billingAddress(BILLING_ADDRESS)
				.email(EMAIL_CUSTOMER2)
				.name(NAME2)
				.password(PASSWORD)
				.paymentMethod(PAYMENT_METHOD)
				.role(CUSTOMER_ROLE)
//				.orders(orders2)
				.build();

		customer3 = Customer.builder()
				.billingAddress(BILLING_ADDRESS)
				.email(EMAIL_CUSTOMER3)
				.name(NAME3)
				.password(PASSWORD)
				.paymentMethod(PAYMENT_METHOD)
				.role(CUSTOMER_ROLE)
//				.orders(orders3)
				.build();

		owner1 = Owner.builder()
				.email(EMAIL_OWNER1)
				.name(NAME1)
				.password(PASSWORD)
				.role(OWNER_ROLE)
				.stores(stores1)
				.build();

		owner2 = Owner.builder()
				.email(EMAIL_OWNER2)
				.name(NAME2)
				.password(PASSWORD)
				.role(OWNER_ROLE)
				.stores(stores2)
				.build();

		owner3 = Owner.builder()
				.email(EMAIL_OWNER3)
				.name(NAME3)
				.password(PASSWORD)
				.role(OWNER_ROLE)
				.stores(stores3)
				.build();

		admin1 = Administrator.builder()
				.email(EMAIL_ADMIN1)
				.name(NAME1)
				.password(PASSWORD)
				.role(ADMIN_ROLE)
				.build();

		admin2 = Administrator.builder()
				.email(EMAIL_ADMIN2)
				.name(NAME2)
				.password(PASSWORD)
				.role(ADMIN_ROLE)
				.build();

		admin3 = Administrator.builder()
				.email(EMAIL_ADMIN3)
				.name(NAME3)
				.password(PASSWORD)
				.role(ADMIN_ROLE)
				.build();

		product1 = Product.builder()
				.description(PRO_DESC1)
				.name(PRO_NAME1)
				.sku(PRO_SKU1)
				.build();

		product2 = Product.builder()
				.description(PRO_DESC2)
				.name(PRO_NAME2)
				.sku(PRO_SKU2)
				.build();

		product3 = Product.builder()
				.description(PRO_DESC3)
				.name(PRO_NAME3)
				.sku(PRO_SKU3)
				.build();

		storeProduct1 = StoreProduct.builder()
				.inventory(INVENTORY1)
				.price(PRICE1)
				.product(product1)
				.build();

		storeProduct2 = StoreProduct.builder()
				.inventory(INVENTORY2)
				.price(PRICE2)
				.product(product2)
				.build();

		storeProduct3 = StoreProduct.builder()
				.inventory(INVENTORY3)
				.price(PRICE3)
				.product(product3)
				.build();

		storeProducts1.add(storeProduct1);
		storeProducts2.add(storeProduct2);
		storeProducts3.add(storeProduct3);

		store1 = Store.builder()
				.address(STORE_ADDRESS1)
				.name(STORE_NAME1)
				.owner(owner1)
//				.storeProducts(storeProducts1)
				.build();

		store2 = Store.builder()
				.address(STORE_ADDRESS2)
				.name(STORE_NAME2)
				.owner(owner2)
//				.storeProducts(storeProducts2)
				.build();

		store3 = Store.builder()
				.address(STORE_ADDRESS3)
				.name(STORE_NAME3)
				.owner(owner3)
//				.storeProducts(storeProducts3)
				.build();


		orderProduct1 = OrderProduct.builder()
				.price(PRICE1)
				.quantity(QUANTITY1)
				.storeProduct(storeProduct1)
				.build();

		orderProduct2 = OrderProduct.builder()
				.price(PRICE2)
				.quantity(QUANTITY2)
				.storeProduct(storeProduct2)
				.build();

		orderProduct3 = OrderProduct.builder()
				.price(PRICE3)
				.quantity(QUANTITY3)
				.storeProduct(storeProduct3)
				.build();

		orderProducts1.add(orderProduct1);
		orderProducts2.add(orderProduct2);
		orderProducts3.add(orderProduct3);

		order1 = Order.builder()
//				.invoiceNumber(INVOICE_NUMBER1)
				.orderProducts(orderProducts1)
				.purchaseDate(PURCHASE_DATE1)
//				.subTotal(SUB_TOTAL1)
//				.total(TOTAL1)
				.build();

		order2 = Order.builder()
//				.invoiceNumber(INVOICE_NUMBER2)
				.orderProducts(orderProducts2)
				.purchaseDate(PURCHASE_DATE2)
//				.subTotal(SUB_TOTAL2)
//				.total(TOTAL2)
				.build();

		order3 = Order.builder()
//				.invoiceNumber(INVOICE_NUMBER3)
				.orderProducts(orderProducts3)
				.purchaseDate(PURCHASE_DATE3)
//				.subTotal(SUB_TOTAL3)
//				.total(TOTAL3)
				.build();

		orders1.add(order1);
		orders2.add(order2);
		orders3.add(order3);
	}

}