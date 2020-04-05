package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ac.project.Robal.models.Account;
import ac.project.Robal.models.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{ 

}
