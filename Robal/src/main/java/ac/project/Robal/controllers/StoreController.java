package ac.project.Robal.controllers;

import ac.project.Robal.models.Product;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.services.StoreService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StoreController {

	private StoreService storeService;
	
	@Autowired
	public StoreController(StoreService storeService) {
		this.storeService = storeService;
	}
	
	@PostMapping("/stores")
	public Store saveStore(@RequestBody Store store) throws Exception {
		return storeService.saveStore(store);
	}
	
	// TODO This should actually return a Store object
	@PostMapping("/stores/{id}/products")
	public StoreProduct saveStoreProduct(@PathVariable Long id, 
								  @RequestBody Product product,
								  @RequestParam Integer quantity,
								  @RequestParam Double price) throws Exception {
		return storeService.saveStoreProduct(id, product, quantity, price);
	}
	
/*	@GetMapping("/stores/{id}/products")
	public List<StoreProduct> findStoreProduct(@PathVariable Long id) throws Exception {
		return storeService.findStoreProducts(id);
	}*/
	
	@GetMapping("/stores/{id}")
	public Store findStore(@PathVariable Long id) {
		return storeService.findStore(id);
	}
	
	@DeleteMapping("/store/{id}")
	public void deleteStore(@PathVariable Long id) throws NotFoundException {
		storeService.deleteStore(id);
	}

	@PutMapping("/store/{id}")
	public Store updateCustomer(@RequestBody Store store) throws Exception {
		return storeService.saveStore(store);
	}
}
