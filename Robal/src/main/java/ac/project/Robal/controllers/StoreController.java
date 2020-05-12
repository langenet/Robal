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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ac.project.Robal.enums.Role;
import ac.project.Robal.models.Account;
import ac.project.Robal.models.Customer;
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

	@ApiOperation(value = "Find a Store by ID", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Store found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER','CUSTOMER')")
	@GetMapping("/stores/{id}")
	public ResponseEntity<Store> findStore(@PathVariable Long id) throws NotFoundException {
		logger.info("***stores method accessed***");
		return new ResponseEntity<>(storeService.findStore(id), HttpStatus.OK);
	}

	@ApiOperation(value = "Find all Stores", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Stores found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasRole('ADMIN','OWNER')") //RL
	@GetMapping("/stores/")
	public ResponseEntity<List<Store>> findStores() throws NotFoundException {
		logger.info("***findStores method accessed***");
		return new ResponseEntity<>(storeService.findStores(), HttpStatus.OK);
	}

	@ApiOperation(value = "Find a StoreProduct by ID", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "StoreProduct found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER','CUSTOMER')")
	@GetMapping("/store-products/{id}")
	public ResponseEntity<StoreProduct> findStoreProduct(@PathVariable Long id) throws Exception {
		logger.info("***findStoreProduct method accessed***");
		return new ResponseEntity<>(storeService.findStoreProduct(id), HttpStatus.OK);
	}

	@ApiOperation(value = "List all StorePoducts", response = List.class) //RL
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Stores found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/stores")
	public ResponseEntity<List<StoreProduct>> listStores() throws Exception {
		return new ResponseEntity<>(storeService.findStoreProducts(), HttpStatus.OK);

	}

	@ApiOperation(value = "List all StoreProduct", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "All Store products found."), 
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER','CUSTOMER')")
	@GetMapping("/store-products")
	public ResponseEntity<List<StoreProduct>> listStoreProducts() throws Exception {
		logger.info("***listStoreProducts method accessed***");
		return new ResponseEntity<>(storeService.findStoreProducts(), HttpStatus.OK);

	}

	@ApiOperation(value = "Find a Stores StoreProduct", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Store's StoreProducts found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@GetMapping("/stores/{id}/store-products")
	public ResponseEntity<List<StoreProduct>> listStoresStoreProducts(Principal principal, @PathVariable Long id)
			throws Exception {

		Account user = AccountUtil.getAccount(principal.getName());
		Owner owner = accountService.findOwner(user.getAccountId());
		Store dbStore = storeService.findStore(id);

		if (dbStore != null) {
			if ((user.getRole() == Role.OWNER
					&& dbStore.getOwner().equals(owner))
					|| user.getRole() == Role.ADMIN) {

				return new ResponseEntity<>(storeService.findStore(id).getStoreProducts(), HttpStatus.OK);

			} else {

				throw new Exception("Only the owner or an admin can view a list of products.");
			}
		} else {
			throw new Exception(
					"This Store does not exist yet.");
		}
	}

	@ApiOperation(value = "List StoreProduct for each owner Id", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Owner's StoreProducts found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@GetMapping("/owner/{id}/store-products/")
	public ResponseEntity<List<StoreProduct>> listOwnerStoreProducts(@PathVariable Long id) throws Exception {
		logger.info("***listOwnerStoreProducts method accessed***");
		return new ResponseEntity<>(storeService.findStore(id).getStoreProducts(), HttpStatus.OK);

	}

	@ApiOperation(value = "Create a Store", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created Store"),
			@ApiResponse(code = 400, message = "Invalid input"),
			@ApiResponse(code = 404, message = "Something didn't work")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PostMapping("/stores")
	public ResponseEntity<Store> saveStore(Principal principal, @RequestBody Store store) throws Exception {

		logger.info("***saveStore method accessed " + " by " + principal.getName() + "***");

		Account user = AccountUtil.getAccount(principal.getName());

		Owner owner;
		if (user.getRole() == Role.ADMIN) {
			owner = accountService.findOwner(store.getOwner().getAccountId());
		} else {
			owner = accountService.findOwnerByEmail(principal.getName());
		}

		Store result = storeService.saveStore(store, owner);

		return ResponseEntity.created(new URI("/stores/" + result.getStoreId())).body(result);
	}

	@ApiOperation(value = "search store products", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "StoreProduct results found."),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER','CUSTOMER')")
	@GetMapping("/store-products/search")
	public ResponseEntity<List<StoreProduct>> searchStoreProducts(Principal principal, @RequestParam("q") String query)
			throws Exception {
		logger.info("***searchStoreProducts method accessed by " + principal.getName() + "***");
		return new ResponseEntity<>(storeService.searchStoreProduct(query), HttpStatus.OK);
	}

	@ApiOperation(value = "Create a StoreProduct", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully created StoreProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PostMapping("/stores/{id}/products")
	public ResponseEntity<StoreProduct> saveStoreProduct(Principal principal,
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

		if ((owner != null && store.getOwner().equals(owner))
				|| user.getRole() == Role.ADMIN) {

//			store.getStoreProducts().add(storeProduct);
			return new ResponseEntity<>(storeService.saveStoreProduct(id, storeProduct),HttpStatus.OK);

		} else {
			throw new Exception("You can only update your own Stores Products unless you are an Administrator.");
		}
	}

	@ApiOperation(value = "Update Store", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully Updated Store"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PutMapping("/stores/{id}")
	public ResponseEntity<Store> updateStore(Principal principal, @RequestBody Store store, @PathVariable Long id)
			throws Exception {
		logger.info("***updateStore method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());
		Owner owner = null;
		Store dbStore = storeService.findStore(id);

		if (store != null) {
			if (user.getRole() == Role.OWNER) {
				owner = accountService.findOwner(user.getAccountId());
			}
		} else {
			throw new Exception(
					"This Store does not exist yet.  Please use the Post method to create a new Store first.");
		}

		if ((owner != null && store.getOwner().equals(owner))
				|| user.getRole() == Role.ADMIN) {

//			store.getStoreProducts().add(storeProduct);
			dbStore = storeService.saveStore(store, owner);
			return ResponseEntity.created(new URI("/stores/" + dbStore.getStoreId())).body(dbStore);

		} else {
			throw new Exception("You can only update your own Stores Products unless you are an Administrator.");
		}

	}

	@ApiOperation(value = "Update StoreProduct", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully Updated StoreProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@PutMapping("/stores/{sid}/store-products/{pid}")
	public ResponseEntity<StoreProduct> updateStoreProduct(Principal principal, @RequestBody StoreProduct storeProduct,
			@PathVariable Long sid, @PathVariable Long pid)
			throws Exception {
		logger.info("***updateStoreProduct method accessed by " + principal.getName() + "***");
		Account user = AccountUtil.getAccount(principal.getName());
		Owner owner = null;
		Store dbStore = storeService.findStore(sid);

		if (dbStore != null) {
			if (user.getRole() == Role.OWNER) {
				owner = accountService.findOwner(user.getAccountId());
			}
		} else {
			throw new Exception(
					"This Store does not exist yet.  Please use the Post method to create a new Store first.");
		}

		if ((owner != null && dbStore.getOwner().equals(owner))
				|| user.getRole() == Role.ADMIN) {

			storeProduct = storeService.saveStoreProduct(sid, storeProduct);
			return ResponseEntity.created(new URI("/stores/" + storeProduct.getStoreProductid())).body(storeProduct);

		} else {
			throw new Exception("You can only update your own Stores Products unless you are an Administrator.");
		}

	}

	@ApiOperation(value = "Delete Store", response = StoreController.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Successfully deleted Store"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
//	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@DeleteMapping("/store/{id}")
	public void deleteStore(Authentication principal, @PathVariable Long id) throws Exception {

		logger.info("***deleteStore method accessed by " + principal.getName() + "***");

		Account user = AccountUtil.getAccount(principal.getName());
		Owner owner = null;
		Store store = storeService.findStore(id);

		if (store != null) {
			if (user.getRole() == Role.OWNER) {
				owner = accountService.findOwner(user.getAccountId());
			}
		} else {
			throw new Exception("This Store does not exist.");
		}

		if ((owner != null && store.getOwner().equals(owner))
				|| user.getRole() == Role.ADMIN) {

			storeService.deleteStore(id);

		} else {
			throw new Exception(
					"You can only delete you own Stores unless you are an Administrator.");
		}
	}

	@ApiOperation(value = "Delete StoreProduct from Store", response = Customer.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully deleted StoreProduct"),
			@ApiResponse(code = 400, message = "Invalid input")
	})
	@PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	@DeleteMapping("/stores/{sid}/store-products/{pid}")
	public void deleteStoreProduct(Principal principal, @PathVariable Long sid, @PathVariable Long pid)
			throws Exception {

		logger.info("***deleteStoreProduct method accessed by " + principal.getName() + "***");

		Account user = AccountUtil.getAccount(principal.getName());
		Owner owner = null;
		Store store = storeService.findStore(sid);

		if (store != null) {
			if (user.getRole() == Role.OWNER) {
				owner = accountService.findOwner(user.getAccountId());
			}
		} else {
			throw new Exception("This Store does not exist.");
		}

		if ((owner != null && store.getOwner().equals(owner))
				|| user.getRole() == Role.ADMIN) {

			storeService.deleteStoreProduct(sid, pid);

		} else {
			throw new Exception(
					"You can only delete storeProduct from your own Stores unless you are an Administrator.");
		}
	}
}
