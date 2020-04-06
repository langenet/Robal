package ac.project.Robal.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name="products")
public class Product {

	@Id
	private Long productId;
	private String name;
	private String description;
	private Long sku;
	
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "Store_Products", 
        joinColumns = { @JoinColumn(name = "store_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
	private List<Store> stores;
}
