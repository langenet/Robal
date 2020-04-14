package ac.project.Robal.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class StoreProduct implements Serializable{

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name="store_id")
	private Store store;
	
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	private int quantity;
	private double price;
	
}
