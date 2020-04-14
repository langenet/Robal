package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.StoreProduct;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct,Long>{

}
