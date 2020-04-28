package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import ac.project.Robal.models.Owner;

@Repository
public interface OwnerRepository  extends JpaRepository<Owner,Long>{
	
	Owner findByEmail(String s);

}
