package ac.project.Robal.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
public class Owner extends Account {

	
//	
//	@OneToOne(cascade = CascadeType.ALL, targetEntity = Store.class)
//	@JoinColumn(name = "account_id", referencedColumnName = "store_id")
//	private Store store;
}
