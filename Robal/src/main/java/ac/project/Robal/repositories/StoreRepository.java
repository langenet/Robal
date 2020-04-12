package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

}
