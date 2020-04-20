package ac.project.Robal.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
@Table(name="products")
public class Product {

	@Id
	@SequenceGenerator(name = "product_seq", sequenceName = "product_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="product_seq")
	private Long productId;
	
	private String name;
	private String description;
	private Long sku;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<StoreProduct> storeProducts;
	
}
