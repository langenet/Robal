package ac.project.Robal.models;

import javax.persistence.Entity;

import ac.project.Robal.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Administrator extends Account{
	
	//remove this
	boolean isAdmin;
}
