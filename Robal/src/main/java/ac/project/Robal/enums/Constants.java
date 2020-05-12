package ac.project.Robal.enums;

import java.util.ArrayList;
import java.util.List;

import ac.project.Robal.models.Administrator;
import ac.project.Robal.models.Customer;
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

//	SupeAdmin
//	Administrator admin = Administrator.builder()
//			.email("super@admin.com")
//			.name("superAdmin")
//			.password("password")
//			.role(Role.ADMIN)
//			.build();

//	protected static final List<Order> ORDERS = Order.builder()
//													.invoiceNumber(1L)
//													.purchaseDate(LocalDate.now())
//													.orderProducts(orderProducts)

	protected static final List<Store> STORES = new ArrayList<>();

	protected static final String STORE_ADDRESS = "123 Store Stree";
	protected static final String STORE_NAME = "Walmart";

	protected static final List<StoreProduct> STORE_PRODUCTS = new ArrayList<>();

	protected static final int INVENTORY = 1;
	protected static final double PRICE = 5.25;

	protected static final String DESCRIPTION = "Toilet Paper";
	protected static final String PRODUCT_NAME = "Charmen";

	protected static final Long SKU = 123L;
	protected static final Long PRODUCT_ID = 1L;

	protected static final Product PRODUCT = Product.builder().description(DESCRIPTION).name(PRODUCT_NAME).sku(SKU)
			.productId(PRODUCT_ID).build();


//	owner = Owner.builder().name(NAME).email(EMAIL).password(PASSWORD).role(ROLE).build();
//
//		STORE_PRODUCTS.add(StoreProduct.builder().inventory(INVENTORY).price(PRICE).product(PRODUCT).build());
//
//		STORES.add(Store.builder().address(STORE_ADDRESS).name(STORE_NAME).owner(owner).storeProducts(STORE_PRODUCTS)
//				.build());


	
	protected static final String UPDATED_NAME  = "superman";
	

	public void setupTests() {

		customer1 = Customer.builder()
				.billingAddress(BILLING_ADDRESS)
				.email(EMAIL_CUSTOMER1)
				.name(NAME1)
				.password(PASSWORD)
				.paymentMethod(PAYMENT_METHOD)
				.role(CUSTOMER_ROLE)
				.build();

		customer2 = Customer.builder()
				.billingAddress(BILLING_ADDRESS)
				.email(EMAIL_CUSTOMER2)
				.name(NAME2)
				.password(PASSWORD)
				.paymentMethod(PAYMENT_METHOD)
				.role(CUSTOMER_ROLE)
				.build();

		customer3 = Customer.builder()
				.billingAddress(BILLING_ADDRESS)
				.email(EMAIL_CUSTOMER3)
				.name(NAME3)
				.password(PASSWORD)
				.paymentMethod(PAYMENT_METHOD)
				.role(CUSTOMER_ROLE)
				.build();

		owner1 = Owner.builder()
				.email(EMAIL_OWNER1)
				.name(NAME1)
				.password(PASSWORD)
				.role(OWNER_ROLE)
				.build();
				
		owner2 = Owner.builder()
				.email(EMAIL_OWNER2)
				.name(NAME2)
				.password(PASSWORD)
				.role(OWNER_ROLE)
				.build();
				
		owner3 = Owner.builder()
				.email(EMAIL_OWNER3)
				.name(NAME3)
				.password(PASSWORD)
				.role(OWNER_ROLE)
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

	}

}
