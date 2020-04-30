package ac.project.Robal.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ac.project.Robal.models.Account;
import ac.project.Robal.utils.AccountUtil;
import lombok.NoArgsConstructor;

@NoArgsConstructor

@Service
public class AccountDetailService implements UserDetailsService {



	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

		Account user = AccountUtil.getAccount(s);


		// Figure out the user's actual role
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(simpleGrantedAuthority);

		return new User(user.getEmail(), 
						user.getPassword(), 
						true, 
						true, 
						true, 
						true, 
						authorities); // authorities);

	}
	
//	private Supplier<UsernameNotFoundException> accountNotFound(String accountType){
//		return () -> new UsernameNotFoundException( accountType + " Not Found.");
//	}

}
