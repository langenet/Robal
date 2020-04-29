package ac.project.Robal.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.Owner;

@Repository
public interface OwnerRepository  extends JpaRepository<Owner,Long>{
	
	Optional<Owner> findByEmail(String s);

}
