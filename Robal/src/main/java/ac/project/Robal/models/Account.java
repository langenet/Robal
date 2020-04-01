package ac.project.Robal.models;

import javax.persistence.Entity;
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
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="USER_ACCOUNTS")
public class Account {

	private Long userId;
	private String userRoll;
	private int storeId;
	

	
	
}
