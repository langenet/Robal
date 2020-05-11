package ac.project.Robal.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@ApiOperation(value = "Find a Product", response = StoreProduct.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Product"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@GetMapping("/products/{id}")
	public Product findProduct(@PathVariable Long id) throws NotFoundException {
		
		logger.info("***findProducts by id method accessed***");
		return productService.findProduct(id);
	}
	
	@ApiOperation(value = "List all Products", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Products"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@GetMapping("/products")
	public ResponseEntity<List<Product>> listProducts() throws NotFoundException {
		
		logger.info("***listProducts controller method accessed***");
		return new ResponseEntity<>(productService.listProducts(), HttpStatus.OK);
	}
	@ApiOperation(value = "Search for Particulate Products", response = StoreProduct.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved Products"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProducts(Principal principal, @RequestParam("q") String query)
			throws Exception {
		logger.info("***searchProducts controller method accessed***");
		return new ResponseEntity<>(productService.searchProduct(query), HttpStatus.OK);
	}

	@ApiOperation(value = "Save Products", response = StoreProduct.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully added StoreProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PostMapping("/products")
	public Product saveProducts(@RequestBody Product products) throws Exception {

		logger.info("***save Products method accessed***");
		return productService.saveProduct(products);
	}

	@ApiOperation(value = "Update a Product", response = StoreProduct.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully updated Product"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN','OWNER')")
	@PutMapping("/products/{id}")
	public Product updateProduct(@RequestBody Product product) throws Exception {
		logger.info("***updateProduct method accessed***");
		return productService.saveProduct(product);
	}

	@ApiOperation(value = "Delete a Product", response = StoreProduct.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Successfully deleted Product"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN','OWNER')")
	@DeleteMapping("/products/{id}")
	public void deleteProduct(@PathVariable Long id) throws NotFoundException {
		logger.info("***deleteProduct method accessed***");
		productService.deleteProduct(id);
	}

	
}
