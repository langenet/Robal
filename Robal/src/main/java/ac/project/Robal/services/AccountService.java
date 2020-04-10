package ac.project.Robal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ac.project.Robal.exceptions.ClientException;
import ac.project.Robal.models.Account;
import ac.project.Robal.repositories.AccountRepository;

@Service
public class AccountService {

	private AccountRepository accountRepository;

	@Autowired
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Account save(Account account) throws Exception {
		if (account.getAccountId() != null && account.getName().isEmpty() && account.getEmail().isEmpty()) {
			throw new ClientException("Cannot create account without a name and email");

		}
		return accountRepository.save(account);
	}
}