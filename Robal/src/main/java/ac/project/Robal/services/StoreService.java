package ac.project.Robal.services;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Product;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.repositories.ProductRepository;
import ac.project.Robal.repositories.StoreProductRepository;
import ac.project.Robal.repositories.StoreRepository;
import javassist.NotFoundException;

@Service
public class StoreService {

	private StoreRepository storeRepository;
	private ProductRepository productRepository;
	private StoreProductRepository storeProductRepository;
	
	
	@Autowired
	public StoreService(StoreRepository storeRepository, 
						StoreProductRepository storeProductRepository,
						ProductRepository productRepository) {
		this.storeRepository = storeRepository;
		this.productRepository = productRepository;
		this.storeProductRepository = storeProductRepository;
	}
	
	
	public Store saveStore(Store store) throws Exception {
		if (store.getName().isEmpty()) {
			throw new ClientException("Cannot create store without a name");
		}
		return storeRepository.save(store);
	}
	
	public StoreProduct saveStoreProduct( Long storeId, Product product, int quantity, double price) throws Exception {
		
		Store store = storeRepository.findById(storeId).orElse(null);
		Product dbProduct = null;
		
		if(store == null) {
			//throw exception
		}
		
		if(product.getProductId() == 0) {
			dbProduct = productRepository.save(product);
		}else {
			dbProduct = productRepository.findById(product.getProductId()).orElse(null);
		}
		
		if(dbProduct == null) {
			dbProduct = productRepository.save(product);
		}
		
		
		
		StoreProduct storeProduct = new StoreProduct(0L, store, dbProduct, quantity, price);
	
		return storeProductRepository.save(storeProduct);
	}
	
	public List<StoreProduct> findStoreProducts(Long storeId) throws Exception{
		List<StoreProduct> storeProducts = storeProductRepository.findByStore(storeId);
		return storeProducts;
	}

	public Store findStore(Long id) {
		return storeRepository.findById(id).orElse(null);
	}

	public void deleteStore(Long id) throws NotFoundException {
		storeRepository.delete(storeRepository.findById(id).orElseThrow(storeNotFound()));
		
	}
	
	private Supplier<NotFoundException> storeNotFound() {
		return () -> new NotFoundException("The store was not found.");
	}

}
