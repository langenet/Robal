package ac.project.Robal.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
		return storeService.saveStoreProduct(id, product, quantity, price, owner);
	}
	
	@GetMapping("/store-products/{id}")
	public StoreProduct findStoreProduct(@PathVariable Long id) throws Exception {
		return storeService.findStoreProduct(id);
	}
	
	// TODO Can anyone see all store products? We could have only authenticated
	// users view them but it's not needed probably
	@GetMapping("/store-products")
	public ResponseEntity<List<StoreProduct>> listStoreProducts(@PathVariable Long id) throws Exception {
		return new ResponseEntity<>(storeService.findStoreProducts(), HttpStatus.OK);

	}

	// TODO search through products as a customer
	// TODO add product to order
	// TODO Submit order

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

//	@PreAuthorize("OWNER")
//	@PutMapping("/store/{id}")
//	public Store updateCustomer(Principal principal, @RequestBody Store store) throws Exception {
//		Owner owner = accountService.findOwnerByEmail(principal.getName());
//		return storeService.saveStore(store, owner);
//	}

	// TODO GET Store owner - Only Admin and the store owner themselves should be
	// able to see the results.
	// TODO PUT update store owner - Only the current store owner can update the
	// owner.
	// Should we create a method in the service that just updates the owner and use
	// that in the store post/put request?


	// TODO PUT storeProducts
	// TODO PUT UpdateStoreProductPrice Only the price in the object will be used
	// with the storeproduct id
	// TODO PUT UpdateStoreProductInventory Only the inventory in the object will be
	// used with the storeproduct id
	// TODO DELETE StoreProduct

}
