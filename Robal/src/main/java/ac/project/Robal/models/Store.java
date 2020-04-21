package ac.project.Robal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


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
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<StoreProduct> storeProducts;
}
