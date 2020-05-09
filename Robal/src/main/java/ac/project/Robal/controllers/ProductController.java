package ac.project.Robal.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Product;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.services.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;

@RestController
public class ProductController {
	
	Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	private ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	@ApiOperation(value = "Save Products", response = StoreProduct.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully added StoreProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PostMapping("/products")
	public Product saveProducts(@RequestBody Product products) throws Exception {
		
		
		logger.info("***saveProducts method accessed***");
		return productService.saveProduct(products);
	}
	@ApiOperation(value = "Find a Product", response = StoreProduct.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Product"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@GetMapping("/products/{id}")
	public Product findProducts(@PathVariable Long id) {
		
		logger.info("***findProducts by id method accessed***");
		return productService.findProduct(id);
	}

	@DeleteMapping("/products/{id}")
	public void deleteeProduct(@PathVariable Long id) throws NotFoundException {
		productService.deleteProduct(id);
	}

	@PutMapping("/products/{id}")
	public Product updateProduct(@RequestBody Product product) throws Exception {
		return productService.saveProduct(product);
	}

	
}
