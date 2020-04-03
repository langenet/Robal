package ac.project.Robal.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Inheritance(strategy=InheritanceType.JOINED)
@Table
public class Store {
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	private String address;
	private String name;

	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
			name = "Store_Products",
			joinColumns = { @JoinColumn(name = "id") },
			inverseJoinColumns = { @JoinColumn(name = "id") }
	)
	private List<Product> products;
	
	
}
