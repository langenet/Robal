package ac.project.Robal.models;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class Administrator extends Account{
	
private boolean isAdmin;

	public Administrator(Account account) {
   		this.setAccountId(account.getAccountId());
   		this.setAccountType(account.getAccountType());
   		this.setEmail(account.getEmail());
   		this.setName(account.getName());
   		this.isAdmin = true;
   	   	}
}
