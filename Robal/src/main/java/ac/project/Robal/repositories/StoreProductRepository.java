package ac.project.Robal.repositories;

import ac.project.Robal.models.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct,Long>{
}
