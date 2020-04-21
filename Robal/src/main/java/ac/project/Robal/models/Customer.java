package ac.project.Robal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class Customer extends Account {

	private String billingAddress;
	private String paymentMethod;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Order> orders;

}
