package ac.project.Robal.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="stores")
public class Store {
	
	@Id
	@SequenceGenerator(name = "store_seq", sequenceName = "store_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="store_seq")
	private Long storeId;
	private String address;
	private String name;


	@ManyToOne
	@JoinColumn(name = "account_id")
	private Owner owner;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<StoreProduct> storeProducts;
	
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts;

}
