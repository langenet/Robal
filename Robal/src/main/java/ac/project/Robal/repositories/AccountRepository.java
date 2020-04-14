package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ac.project.Robal.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{

}
