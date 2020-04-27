package ac.project.Robal.services;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.enums.Role;
import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Owner;
import ac.project.Robal.models.Product;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.repositories.OwnerRepository;
import ac.project.Robal.repositories.ProductRepository;
import ac.project.Robal.repositories.StoreProductRepository;
import ac.project.Robal.repositories.StoreRepository;
import javassist.NotFoundException;

@Service
public class StoreService {

	private StoreRepository storeRepository;
	private ProductRepository productRepository;
	private StoreProductRepository storeProductRepository;
	private OwnerRepository ownerRepository;

	@Autowired
	public StoreService(StoreRepository storeRepository, StoreProductRepository storeProductRepository,
			ProductRepository productRepository, OwnerRepository ownerRepository) {
		this.storeRepository = storeRepository;
		this.productRepository = productRepository;
		this.storeProductRepository = storeProductRepository;
		this.ownerRepository = ownerRepository;
	}

	public Store saveStore(Store store) throws Exception {
		// TODO add owner here

		Owner storeOwner = ownerRepository.findById(store.getOwner().getAccountId()).orElse(null);
		if (storeOwner.getRole() != Role.OWNER) {
			throw new ClientException("Cannot create store without an Owner Account");
		}

		if (store.getName() == null || store.getAddress() == null) {
			throw new ClientException("Cannot create store without a name");
		}
		if (store.getStoreId() == null || store.getStoreId() == 0) {

			Owner owner = ownerRepository.findById(store.getOwner().getAccountId()).orElse(null);
			if (owner == null) {

				// TODO validate owner first
				store.getOwner().getStores().add(store);
				owner = Owner.builder().email(store.getOwner().getEmail()).name(store.getOwner().getName())
						.password(store.getOwner().getPassword()).role(store.getOwner().getRole())
						.stores(store.getOwner().getStores()) // TODO this might loop
						.build();
			}

			store.setOwner(owner);

			return storeRepository.save(store);
		} else {
			Store dbStore = storeRepository.findById(store.getStoreId()).orElseThrow(storeNotFound());

			dbStore.setName(store.getName());
			dbStore.setAddress(store.getAddress());

			Owner owner = ownerRepository.findById(store.getOwner().getAccountId()).orElse(null);
			if (owner == null) {

				// TODO Validate Owner
				store.getOwner().getStores().add(dbStore);
				owner = Owner.builder().email(store.getOwner().getEmail()).name(store.getOwner().getName())
						.password(store.getOwner().getPassword()).role(store.getOwner().getRole())
						.stores(store.getOwner().getStores()) // TODO this might loop
						.build();
			}

			dbStore.setOwner(owner);

			// TODO validation that the store product is complete
			for (StoreProduct storeProduct : store.getStoreProducts()) {
				if (!dbStore.getStoreProducts().contains(storeProduct)) {
					dbStore.getStoreProducts().add(storeProduct);
				}
			}

			// TODO not sure if we need to saveAll storeproducts first
			return storeRepository.save(dbStore);
		}
	}

	public StoreProduct saveStoreProduct(Long storeId, Product product, int inventory, double price) throws Exception {

		Product dbProduct = productRepository.findById(product.getProductId()).orElse(null);
		if (dbProduct == null) {
			dbProduct = Product.builder().description(product.getDescription()).name(product.getName())
					.sku(product.getSku()).build();
		}

		StoreProduct storeProduct = StoreProduct.builder().inventory(inventory).price(price).product(dbProduct).build();

		storeProduct = storeProductRepository.save(storeProduct);

		Store dbStore = storeRepository.findById(storeId).orElseThrow(storeNotFound());

		// TODO validation that the store product is complete
//		for(StoreProduct dbStoreProduct: dbStore.getStoreProducts()) {
		// TODO verify if that specific Product ID exists in the list. If it does,
		// update the inventory and price only

//			if (!dbStore.getStoreProducts().contains(storeProduct)) {
		dbStore.getStoreProducts().add(storeProduct);
//			}
//		}

//		storeProduct = storeProductRepository.save(storeProduct);

		dbStore = storeRepository.save(dbStore);

		// TODO verify if there is a better way of returning the storeProduct
		return storeProduct;
	}

	/*
	 * public List<StoreProduct> findStoreProducts(Long storeId) throws Exception{
	 * List<StoreProduct> storeProducts =
	 * storeProductRepository.findByStore(storeId); return storeProducts; }
	 */

	public Store findStore(Long id) throws NotFoundException {
		return storeRepository.findById(id).orElseThrow(storeNotFound());
	}

	public void deleteStore(Long id) throws NotFoundException {
		storeRepository.delete(storeRepository.findById(id).orElseThrow(storeNotFound()));

	}

	private Supplier<NotFoundException> storeNotFound() {
		return () -> new NotFoundException("The store was not found.");
	}

	private Supplier<NotFoundException> accountNotFound() {
		// TODO Logging
		return () -> new NotFoundException("The account was not found.");
	}
}
