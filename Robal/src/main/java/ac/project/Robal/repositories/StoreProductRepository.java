package ac.project.Robal.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.StoreProduct;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct,Long>{

	List<StoreProduct> findByProduct_NameOrProduct_DescriptionContainingIgnoreCase(String name, String description);
}
