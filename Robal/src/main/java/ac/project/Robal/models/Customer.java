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
	
	
	   	@ManyToOne(optional = false)
	    @JoinColumn(name = "order_id")
	    private List<Order> orders;

}
