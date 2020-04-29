package ac.project.Robal.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>{

	Optional<Customer> findByEmail(String s);
}
