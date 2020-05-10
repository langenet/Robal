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
	private ProductService productService;

	@Autowired
	public StoreService(StoreRepository storeRepository, StoreProductRepository storeProductRepository,
			ProductRepository productRepository, OwnerRepository ownerRepository,
			ProductService productService) {
		this.storeRepository = storeRepository;
		this.productRepository = productRepository;
		this.storeProductRepository = storeProductRepository;
		this.ownerRepository = ownerRepository;
		this.productService = productService;
	}

	public Store saveStore(Store store, Owner authOwner) throws NotFoundException, ClientException {

		if (store.getName() == null || store.getAddress() == null) {
			throw new ClientException("Cannot create store without a name or address.");
		}

		// New store
		if (store.getStoreId() == null || store.getStoreId() == 0) {

			store.setOwner(authOwner);

			return storeRepository.save(store);

		} else {

			Store dbStore = storeRepository.findById(store.getStoreId()).orElseThrow(storeNotFound());
			if (dbStore.getOwner() == authOwner) {

				if (store.getOwner() != null) {
					Owner newOwner = ownerRepository.findById(store.getOwner().getAccountId()).orElse(null);
					if (newOwner != null
							&& newOwner != authOwner) {
						dbStore.setOwner(newOwner);
					} else {
						throw new NotFoundException("New Owner doesn't exist in the database yet.");
					}

				}

				dbStore.setName(store.getName());
				dbStore.setAddress(store.getAddress());

				// TODO validation that the store product is complete
				for (StoreProduct storeProduct : store.getStoreProducts()) {
					if (!dbStore.getStoreProducts().contains(storeProduct)) {
						dbStore.getStoreProducts().add(storeProduct);
					}
				}

				return storeRepository.save(dbStore);

			} else {
				throw new NotFoundException("Only the Owner of the Store can modify it");
			}
		}
	}

	public StoreProduct saveStoreProduct(Long storeId, StoreProduct storeProduct, Owner owner)
			throws Exception {

		Store dbStore = storeRepository.findById(storeId).orElseThrow(storeNotFound());

		Product product = storeProduct.getProduct();
		int inventory = storeProduct.getInventory();
		double price = storeProduct.getPrice();

		if (dbStore.getOwner().equals(owner)) {

			Product dbProduct = productRepository.findById(product.getProductId()).orElse(null);
			if (dbProduct == null) {
				dbProduct = productService.saveProduct(product);
			}
			
			if (inventory < 0) {
				throw new Exception("Must be a positive Inventory numer.");
			} else if (price < 0) {
				throw new Exception("Must be a positive Pricer.");
			} else {

//				StoreProduct storeProduct = StoreProduct.builder().inventory(inventory).price(price).product(dbProduct)
//						.build();
				for (StoreProduct dbStoreProduct : dbStore.getStoreProducts()) {
					if (dbStoreProduct.getProduct().getProductId() == storeProduct.getProduct().getProductId()) {
						dbStoreProduct.setInventory(storeProduct.getInventory());
						dbStoreProduct.setPrice(storeProduct.getPrice());

						dbStore = storeRepository.save(dbStore);
						return dbStoreProduct;
					}
				}

				storeProduct.setProduct(dbProduct);
				storeProduct = storeProductRepository.save(storeProduct);

				dbStore.getStoreProducts().add(storeProduct);
				dbStore = storeRepository.save(dbStore);

				return storeProduct;
			}
		} else {

			throw new NotFoundException("Only the owner of the store can modify it.");
		}
	}

	public StoreProduct findStoreProduct(Long id) throws Exception {
		return storeProductRepository.findById(id).orElseThrow(storeProductNotFound());
	}

	public List<StoreProduct> searchStoreProduct(String query) throws Exception {

		return storeProductRepository.findByProduct_NameOrProduct_DescriptionContainingIgnoreCase(query, query);
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
