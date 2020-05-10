package ac.project.Robal.services;

import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.models.Product;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.repositories.ProductRepository;
import javassist.NotFoundException;

@Service
public class ProductService {

	Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	private ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product saveProduct(Product product) throws Exception {


		if (product.getProductId() == null || product.getProductId() == 0) {
			if (product.getName() != null) {
				
				return productRepository.save(product);
			} else {
				throw new Exception("Product must have a name");
			}
		} else {
			
			Product dbProduct = productRepository.findById(product.getProductId()).orElseThrow(productNotFound());
			
			dbProduct.setDescription(product.getDescription());
			dbProduct.setName(product.getName());
			dbProduct.setSku(product.getSku());
			
			return productRepository.save(dbProduct);
		}
		
	}

	public Product findProduct(Long id) throws NotFoundException {
		logger.info("***findProducts by id method accessed***");
		return productRepository.findById(id).orElseThrow(productNotFound());
	}
	
	public List<Product> listProducts() throws NotFoundException {
		logger.info("***findProducts by id method accessed***");
		return productRepository.findAll();
	}


	public List<Product> searchProduct(String query) throws Exception {

		return productRepository.findByNameOrDescriptionContainingIgnoreCase(query, query);
	}
	
	public void deleteProduct(Long id) throws NotFoundException {
		productRepository.delete(productRepository.findById(id).orElseThrow(productNotFound()));
	}

	private Supplier<NotFoundException> productNotFound() {
		return () -> new NotFoundException("The order was not found.");
	}
}
