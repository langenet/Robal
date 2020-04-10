package ac.project.Robal.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class Customer extends Account {
	
	
	   	@ManyToOne(optional = true, targetEntity = Order.class)
	    @JoinColumn(name = "order_id")
	    private List<Order> orders;

	   	public Customer(Account account) {
	   		this.setAccountId(account.getAccountId());
	   		this.setAccountType(account.getAccountType());
	   		this.setEmail(account.getEmail());
	   		this.setName(account.getName());
	   	}
	   	
}
