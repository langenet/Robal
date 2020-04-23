package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.Customer;
import ac.project.Robal.models.Owner;

@Repository
public interface OwnerRepository  extends JpaRepository<Owner,Long>{
	
	Owner findByEmail(String s);

}
