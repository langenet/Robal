package ac.project.Robal.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.models.Order;
import ac.project.Robal.models.Product;
import ac.project.Robal.services.ProductService;
import javassist.NotFoundException;

@RestController
public class ProductController {
	
	private ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping("/products")
	public Product saveProducts(@RequestBody Product products) throws Exception {
		return productService.saveProduct(products);
	}

	@GetMapping("/prodcuts/{id}")
	public Product findProducts(@PathVariable Long id) {
		return productService.findProduct(id);
	}

	@DeleteMapping("/prodcuts/{id}")
	public void deleteOrder(@PathVariable Long id) throws NotFoundException {
		productService.deleteProduct(id);
	}

	@PutMapping("/prodcuts/{id}")
	public Product updateOrder(@RequestBody Product product) throws Exception {
		return productService.saveProduct(product);
	}

	
}
