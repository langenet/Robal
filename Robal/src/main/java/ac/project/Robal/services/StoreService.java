package ac.project.Robal.services;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Store saveStore(Store store, Owner authOwner) throws NotFoundException, ClientException {

		if (store.getName() == null || store.getAddress() == null || store.getOwner().getAccountId() == null
				|| store.getOwner().getAccountId() == 0) {
			throw new ClientException("Cannot create store without a name, address or Owner");
		}

		Owner newOwner = ownerRepository.findById(store.getOwner().getAccountId()).orElse(null);

		if (newOwner != null) {

			// New store
			if (store.getStoreId() == null || store.getStoreId() == 0) {

				// compare if owners are the same

				if (newOwner == authOwner) {

					// Authenticated Owner is the same owner being set for this store.
					store.setOwner(authOwner);

				}

				return storeRepository.save(store);

			} else {
				Store dbStore = storeRepository.findById(store.getStoreId()).orElseThrow(storeNotFound());

				dbStore.setName(store.getName());
				dbStore.setAddress(store.getAddress());

				dbStore.setOwner(newOwner);

				// TODO validation that the store product is complete
				for (StoreProduct storeProduct : store.getStoreProducts()) {
					if (!dbStore.getStoreProducts().contains(storeProduct)) {
						dbStore.getStoreProducts().add(storeProduct);
					}
				}

				// TODO not sure if we need to saveAll storeproducts first
				return storeRepository.save(dbStore);
			}
		} else {
			throw new NotFoundException("Only the Owner of the Store can modify it");
		}
	}

	public StoreProduct saveStoreProduct(Long storeId, Product product, int inventory, double price, Owner owner)
			throws Exception {

		Store dbStore = storeRepository.findById(storeId).orElseThrow(storeNotFound());

		if (dbStore.getOwner().equals(owner)) {

			Product dbProduct = productRepository.findById(product.getProductId()).orElse(null);
			if (dbProduct == null) {
				if (product.getName() != null) {

					dbProduct = Product.builder().description(product.getDescription()).name(product.getName())
							.sku(product.getSku()).build();
					dbProduct = productRepository.save(product);
				} else {
					throw new Exception("Product must have a name");
				}
			}
			if (inventory < 0) {
				throw new Exception("Must be a positive Inventory numer.");
			} else if (price < 0) {
				throw new Exception("Must be a positive Pricer.");
			} else {

				StoreProduct storeProduct = StoreProduct.builder().inventory(inventory).price(price).product(dbProduct)
						.build();

				storeProduct = storeProductRepository.save(storeProduct);
				// TODO verify if that specific Product ID exists in the list. If it does,
				// if (!dbStore.getStoreProducts().contains(storeProduct)) {

				dbStore.getStoreProducts().add(storeProduct);

				dbStore = storeRepository.save(dbStore);

				return storeProduct;
			}
		} else {

			// TODO verify if there is a better error exceptiom
			throw new NotFoundException("Only the owner of the store can modify it.");
		}
	}

	public StoreProduct findStoreProduct(Long id) throws Exception {
		return storeProductRepository.findById(id).orElseThrow(storeProductNotFound());
	}

	public List<StoreProduct> findStoreProducts() throws Exception {
		return storeProductRepository.findAll();
	}

	public Store findStore(Long id) throws NotFoundException {
		return storeRepository.findById(id).orElseThrow(storeNotFound());
	}

	public void deleteStore(Long id) throws NotFoundException {
		storeRepository.delete(storeRepository.findById(id).orElseThrow(storeNotFound()));

	}

	private Supplier<NotFoundException> storeProductNotFound() {
		return () -> new NotFoundException("The storeProduct was not found.");
	}

	private Supplier<NotFoundException> storeNotFound() {
		return () -> new NotFoundException("The store was not found.");
	}

	private Supplier<NotFoundException> accountNotFound() {
		// TODO Logging
		return () -> new NotFoundException("The Owner was not found.");
	}

}
