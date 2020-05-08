package ac.project.Robal.controllers;

import java.net.URI;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.enums.Role;
import ac.project.Robal.models.Account;
import ac.project.Robal.models.Owner;
import ac.project.Robal.models.Store;
import ac.project.Robal.models.StoreProduct;
import ac.project.Robal.services.AccountService;
import ac.project.Robal.services.StoreService;
import ac.project.Robal.utils.AccountUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javassist.NotFoundException;

@RestController
public class StoreController {

	Logger logger = LoggerFactory.getLogger(StoreController.class);

	private StoreService storeService;
	private AccountService accountService;

	@Autowired
	public StoreController(StoreService storeService,
			AccountService accountService) {
		this.storeService = storeService;
		this.accountService = accountService;
	}

	@ApiOperation(value = "Create a Store", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Store"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PostMapping("/stores")
	public ResponseEntity<Store> saveStore(Principal principal, @RequestBody Store store) throws Exception {

		logger.info("***saveStore method accessed " + " by " + principal.getName() + "***");

		Owner owner = accountService.findOwnerByEmail(principal.getName());

		Store result = storeService.saveStore(store, owner);

		return ResponseEntity.created(new URI("/owners/" + result.getStoreId())).body(result);
	}

	@ApiOperation(value = "Create a StoreProduct", response = Owner.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created StoreProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PostMapping("/stores/{id}/products")
	public StoreProduct saveStoreProduct(Principal principal,
			@PathVariable Long id,
			@RequestBody StoreProduct storeProduct) throws Exception {

		logger.info("***updateOrder method accessed by " + principal.getName() + "***");

		Account user = AccountUtil.getAccount(principal.getName());
		Owner owner = null;
		Store store = storeService.findStore(id);

		
		if (store != null) {
			if (user.getRole() == Role.OWNER) {
				owner = accountService.findOwner(user.getAccountId());
			}
		} else {
			throw new Exception(
					"This Store does not exist yet.  Please use the Post method to create a new Store first.");
		}

		if ((owner != null && owner.getStores().contains(store))
				|| user.getRole() == Role.ADMIN) {

			store.getStoreProducts().add(storeProduct);
			return storeService.saveStoreProduct(id, storeProduct, owner);

		} else {
			throw new Exception("You can only update your own orders unless you are an Administrator.");
		}
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
