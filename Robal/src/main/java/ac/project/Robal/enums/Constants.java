package ac.project.Robal.enums;

import java.util.ArrayList;
import java.util.List;

import ac.project.Robal.models.Product;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;

public class Constants {

	protected static final String NAME = "Andy Ta";
	protected static final String EMAIL = "Andy@test.com";
	protected static final String PASSWORD = "password";
	
	protected static final Role ROLE = Role.CUSTOMER;
//	protected static final List<Order> ORDERS = new ArrayList<Order>();
	protected static final String BILLING_ADDRESS = "123 Main Street";
	protected static final String PAYMENT_METHOD = "MasterCard";
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

}
