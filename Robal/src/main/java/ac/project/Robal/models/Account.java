package ac.project.Robal.models;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import ac.project.Robal.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="accounts")
public class Account {

	@Id
	@GeneratedValue
	private Long accountId;
	private String name;
	private String email;
	
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	


	
	
}
