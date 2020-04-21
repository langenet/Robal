package ac.project.Robal.models;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class StoreProduct {

	@Id
	@SequenceGenerator(name = "store_product_seq", sequenceName = "store_product_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="store_product_seq")
	private Long storeProductid;
	
	@ManyToOne
	private Product product;
	
	private int inventory;
	private double price;
	
}
