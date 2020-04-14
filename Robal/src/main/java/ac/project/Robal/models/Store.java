package ac.project.Robal.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name="stores")
public class Store {
	
	@Id
	@GeneratedValue
	private Long storeId;
	private String address;
	private String name;


	@ManyToOne(targetEntity = Store.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id", referencedColumnName="storeId")
	private Owner owner;
	
//	//verify how we could add extra columns to this for price and quantity.
//	@ManyToMany(cascade = { CascadeType.ALL })
//    @JoinTable(
//        name = "Store_Products", 
//        joinColumns = { @JoinColumn(name = "store_id") }, 
//        inverseJoinColumns = { @JoinColumn(name = "product_id") }
//    )
//	private List<Product> products;
//	
}
