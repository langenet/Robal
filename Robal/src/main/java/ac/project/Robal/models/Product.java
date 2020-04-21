package ac.project.Robal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
@Table
public class Product {

	@Id
	@SequenceGenerator(name = "product_seq", sequenceName = "product_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_seq")
	private Long productId;
	
	private String name;
	private String description;
	private Long sku;
}
