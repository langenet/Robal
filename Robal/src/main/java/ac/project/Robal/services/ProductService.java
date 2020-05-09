package ac.project.Robal.services;

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

	public Product saveProduct(Product product) throws Exception {

		// TODO: add validation on the mandatory fields
//		if (order.getInvoiceNumber().isEmpty() && order.getEmail().isEmpty()) {
//			throw new ClientException("Cannot create Customer without a name and email");
//		}
		return productRepository.save(product);
	}

	public Product findProduct(Long id) {
		logger.info("***findProducts by id method accessed***");
		return productRepository.findById(id).orElse(null);
	}

	public void deleteProduct(Long id) throws NotFoundException {
		productRepository.delete(productRepository.findById(id).orElseThrow(productNotFound()));
	}

	private Supplier<NotFoundException> productNotFound() {
		return () -> new NotFoundException("The order was not found.");
	}
}
