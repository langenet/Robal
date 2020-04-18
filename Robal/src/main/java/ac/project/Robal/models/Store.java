package ac.project.Robal.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
	@SequenceGenerator(name = "store_seq", sequenceName = "store_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="store_seq")
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
