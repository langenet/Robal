package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.OrderProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct,Long>{

}