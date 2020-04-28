package ac.project.Robal.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.models.Owner;
import ac.project.Robal.models.Product;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.services.AccountService;
import ac.project.Robal.services.StoreService;
import javassist.NotFoundException;

@RestController
public class StoreController {

	private StoreService storeService;
	private AccountService accountService;
	
	@Autowired
	public StoreController(StoreService storeService,
							AccountService accountService) {
		this.storeService = storeService;
		this.accountService = accountService;
	}
	
	@PreAuthorize("OWNER")
	@PostMapping("/stores")
	public Store saveStore(Principal principal, @RequestBody Store store) throws Exception {
		Owner owner = accountService.findOwnerByEmail(principal.getName());
		return storeService.saveStore(store, owner);
	}
	
	// TODO This should actually return a Store object
	@PreAuthorize("OWNER")
	@PostMapping("/stores/{id}/products")
	public StoreProduct saveStoreProduct(Principal principal,
								  @PathVariable Long id, 
								  @RequestBody Product product,
								  @RequestParam Integer quantity,
								  @RequestParam Double price) throws Exception {
		Owner owner = accountService.findOwnerByEmail(principal.getName());
		//TODO add owner to saveStoreProduct parameters and test that it is the owner trying to save a product.
		return storeService.saveStoreProduct(id, product, quantity, price);
	}
	
/*	@GetMapping("/stores/{id}/products")
	public List<StoreProduct> findStoreProduct(@PathVariable Long id) throws Exception {
		return storeService.findStoreProducts(id);
	}*/
	
	@GetMapping("/stores/{id}")
	public Store findStore(@PathVariable Long id) throws NotFoundException {
		return storeService.findStore(id);
	}
	
	@PreAuthorize("OWNER")
	@DeleteMapping("/store/{id}")
	public void deleteStore(Principal principal, @PathVariable Long id) throws NotFoundException {
		Owner owner = accountService.findOwnerByEmail(principal.getName());
		storeService.deleteStore(id);
	}

	@PreAuthorize("OWNER")
	@PutMapping("/store/{id}")
	public Store updateCustomer(Principal principal, @RequestBody Store store) throws Exception {
		Owner owner = accountService.findOwnerByEmail(principal.getName());
		return storeService.saveStore(store, owner);
	}
}
