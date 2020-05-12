package ac.project.Robal.services;

import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.enums.Role;
import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Account;
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

	Logger logger = LoggerFactory.getLogger(StoreService.class);
	
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

	public Store findStore(Long id) throws NotFoundException {
		logger.info("***service findStore by id method accessed.***");
		return storeRepository.findById(id).orElseThrow(storeNotFound());
	}

	public List<Store> findStores() throws NotFoundException {
		logger.info("***service findAllStores method accessed.***");
		return storeRepository.findAll();
	}

	public StoreProduct findStoreProduct(Long id) throws Exception {
		logger.info("***service findStoreProduct method accessed by Id: " + id + "***");
		return storeProductRepository.findById(id).orElseThrow(storeProductNotFound());
	}

	public List<StoreProduct> findStoreProducts() throws Exception {
		logger.info("***service findAllStoreProducts method accessed.***");
		return storeProductRepository.findAll();
	}

	public List<StoreProduct> searchStoreProduct(String query) throws Exception {
		logger.info("***service searchStoreProducts method accessed.***");
		return storeProductRepository.findByProduct_NameOrProduct_DescriptionContainingIgnoreCase(query, query);
	}

	public Store saveStore(Store store, Account authOwner) throws NotFoundException, ClientException {
		logger.info("***service saveStore method accessed.***");
		if (store.getName() == null || store.getAddress() == null) {
			throw new ClientException("Cannot create store without a name or address.");
		}
		
		Owner storeOwner = null;
		// New store
		if (store.getStoreId() == null || store.getStoreId() == 0) {
			if (authOwner.getRole() == Role.ADMIN
					&& store.getOwner() != null) {
				storeOwner = ownerRepository.findById(store.getOwner().getAccountId()).orElseThrow(ownerNotFound());
			} else {
				storeOwner = (Owner) authOwner;
			}
			
			store.setOwner(storeOwner);

			return storeRepository.save(store);

		} else {

			Store dbStore = storeRepository.findById(store.getStoreId()).orElseThrow(storeNotFound());
			if (dbStore.getOwner().getAccountId() == authOwner.getAccountId()
					|| authOwner.getRole() == Role.ADMIN) {


				if (store.getOwner() != null) {
					Owner newOwner = ownerRepository.findById(store.getOwner().getAccountId()).orElse(null);
					if (newOwner != null
							&& newOwner.getAccountId() != authOwner.getAccountId()) {
						dbStore.setOwner(newOwner);
					} else {
						throw new NotFoundException("New Owner doesn't exist in the database yet.");
					}

				}

				dbStore.setName(store.getName());
				dbStore.setAddress(store.getAddress());

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

	public StoreProduct saveStoreProduct(Long storeId, StoreProduct storeProduct)
			throws Exception {

		Store dbStore = storeRepository.findById(storeId).orElseThrow(storeNotFound());

		Product product = storeProduct.getProduct();
		int inventory = storeProduct.getInventory();
		double price = storeProduct.getPrice();

//		if (dbStore.getOwner().equals(owner)) {

		Product dbProduct = productRepository.findById(product.getProductId()).orElse(null);
		if (dbProduct == null) {
			dbProduct = productService.saveProduct(product);
		}

		if (inventory < 0) {
			throw new Exception("Must be a positive Inventory number.");
		} else if (price < 0) {
			throw new Exception("Must be a positive Price");
		} else {

			for (StoreProduct dbStoreProduct : dbStore.getStoreProducts()) {
				if (dbStoreProduct.getProduct().getProductId() == storeProduct.getProduct().getProductId()) {

					dbStoreProduct.setInventory(storeProduct.getInventory());
					dbStoreProduct.setPrice(storeProduct.getPrice());

					dbStore = storeRepository.save(dbStore);
					return dbStoreProduct;
				}
			}

			storeProduct.setProduct(dbProduct);
			logger.info("***Repo: saveStoreProduct save attempted.***");
			storeProduct = storeProductRepository.save(storeProduct);

			dbStore.getStoreProducts().add(storeProduct);
			logger.info("***Repo: saveStoreProduct(add) service attempted.***");
			dbStore = storeRepository.save(dbStore);			
			return storeProduct;
		}
	}

	public void deleteStore(Long id) throws NotFoundException {
		logger.info("***deleteStore by Id " + id + "***");
		storeRepository.delete(storeRepository.findById(id).orElseThrow(storeNotFound()));

	}

	public void deleteStoreProduct(Long storeId, Long storeProductId) throws NotFoundException {

		Store store = storeRepository.findById(storeId).orElseThrow(storeNotFound());

		logger.info("***deleteStoreProduct by Id " + storeProductId + "***");
		store.getStoreProducts().removeIf(storeProduct -> storeProductId.equals(storeProduct.getStoreProductid()));
		storeRepository.save(store);
	}

	private Supplier<NotFoundException> storeNotFound() {
		return () -> {
		logger.info("***storeNotFound Exception***");
		return new NotFoundException("The store was not found.");
		};
	}

	private Supplier<NotFoundException> storeProductNotFound() {
		return () -> {
			logger.info("***storeProductNotFound Exception***");
			return new NotFoundException("The StoreProduct does not exist.");
		};
	}

	private Supplier<NotFoundException> ownerNotFound() {
		return () -> {
			logger.info("***ownerNotFound Exception***");
			return new NotFoundException("The owner was not set or does not exist.");
		};
	}
}
