package ac.project.Robal.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Customer {
	
	   	@ManyToOne(optional = false)
	    @JoinColumn(name = "order_id")
	    private Order orders;

}
