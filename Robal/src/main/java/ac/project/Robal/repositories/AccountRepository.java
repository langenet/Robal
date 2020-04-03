package ac.project.Robal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ac.project.Robal.models.Account;

public interface AccountRepository extends JpaRepository<Account,Integer>{

}
