package ac.project.Robal.services;

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
	
	public StoreProduct saveStoreProduct(Product product, Long id) throws Exception {
		
		StoreProduct storeProduct = new StoreProduct();
//		if (product.getName().isEmpty()) {
//			throw new ClientException("Cannot create store without a name");
//		}
		return storeProductRepository.save(storeProduct);
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
