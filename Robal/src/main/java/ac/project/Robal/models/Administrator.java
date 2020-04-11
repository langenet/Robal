package ac.project.Robal.models;

import javax.persistence.Entity;

import ac.project.Robal.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Entity
public class Administrator extends Account{
	
	
	public Administrator() {
		
	}
	public Administrator(Account account) {
   		this.setAccountId(account.getAccountId());
   		this.setAccountType(account.getAccountType());
   		this.setEmail(account.getEmail());
   		this.setName(account.getName());
   	   	}
	boolean isAdmin;
}
