package ac.project.Robal.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name="STORES")
public class Store {
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private int storeId;
	private String branchAdd;
	private String name;
	
	
	
}
