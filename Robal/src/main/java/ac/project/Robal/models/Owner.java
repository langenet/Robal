package ac.project.Robal.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class Owner extends Account {

	//	
	@OneToOne
	private Store store;
	
 	public Owner(Account account) {
   		this.setAccountId(account.getAccountId());
   		this.setAccountType(account.getAccountType());
   		this.setEmail(account.getEmail());
   		this.setName(account.getName());
   	//	this.setStore(store);

   	}
	
}
