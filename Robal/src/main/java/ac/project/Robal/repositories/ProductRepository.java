package ac.project.Robal.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.Product;
import ac.project.Robal.models.StoreProduct;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{ 
	
	List<Product> findByNameOrDescriptionContainingIgnoreCase(String name, String description);

}
