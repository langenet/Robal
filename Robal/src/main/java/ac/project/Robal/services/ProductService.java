package ac.project.Robal.services;

import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.models.Product;
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

	public Product findProduct(Long id) throws NotFoundException {
		logger.info("***findProducts by id method accessed***");
		return productRepository.findById(id).orElseThrow(productNotFound(id));
	}

	public List<Product> listProducts() throws NotFoundException {
		logger.info("***listProducts method accessed***");
		return productRepository.findAll();
	}

	public List<Product> searchProduct(String query) throws Exception {
		logger.info("***searchProducts service method accessed***");
		return productRepository.findByNameOrDescriptionContainingIgnoreCase(query, query);
	}

	public Product saveProduct(Product product) throws Exception {

		if (product.getProductId() == null || product.getProductId() == 0) {
			if (product.getName() != null) {

				return productRepository.save(product);
			} else {
				throw new Exception("Product must have a name");
			}
		} else {

			Product dbProduct = productRepository.findById(product.getProductId())
					.orElseThrow(productNotFound(product.getProductId()));

			dbProduct.setDescription(product.getDescription());
			dbProduct.setName(product.getName());
			dbProduct.setSku(product.getSku());
			logger.info("***Repo:saveProducts service method accessed***");
			return productRepository.save(dbProduct);
		}

	}

	public void deleteProduct(Long id) throws NotFoundException {
		logger.info("***Repo:deleteProduct service by Id " + id + "***");
		productRepository.delete(productRepository.findById(id).orElseThrow(productNotFound(id)));
	}

	private Supplier<NotFoundException> productNotFound(Long id) {
		return () -> {
			logger.info("***Product Lookup failed. Id " + id + " ***");
			return new NotFoundException("The product was not found.");
		};
	}
}
