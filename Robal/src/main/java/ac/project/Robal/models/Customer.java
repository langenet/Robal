package ac.project.Robal.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import ac.project.Robal.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter

@Entity
public class Customer extends Account {

	private String billingAddress;
	private String paymentMethod;

	@Builder
	public Customer (Long accountId, 
					 String name,
					 String email,
					 String password,
					 Role role,
					 String billingAddress, 
					 String paymentMethod, 
					 List<Order> orders) {
		super(accountId,name,email,password, role);
		this.billingAddress = billingAddress;
		this.paymentMethod = paymentMethod;
		this.orders = orders;
	}

	@OneToMany(cascade = CascadeType.ALL)
	private List<Order> orders;

}
