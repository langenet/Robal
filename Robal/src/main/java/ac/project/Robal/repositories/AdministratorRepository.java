package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.Administrator;

@Repository
public interface AdministratorRepository  extends JpaRepository<Administrator,Long>{

}
