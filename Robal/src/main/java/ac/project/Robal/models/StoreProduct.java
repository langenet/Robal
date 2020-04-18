package ac.project.Robal.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class StoreProduct {

	@Id
	@SequenceGenerator(name = "store_product_seq", sequenceName = "store_product_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="store_product_seq")
	private Long storeProductid;

	@ManyToOne
	@JoinColumn(name="store_id")
	private Store store;
	
	
	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;
	
	private int inventory;
	private double price;
	
}
