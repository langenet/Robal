package ac.project.Robal.models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import ac.project.Robal.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter


// TODO cascade deletes. not sure where to add that.  Deleting a customer from the account table did not cascade.
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="accounts")
public class Account {

	@Id
	@SequenceGenerator(name = "account_seq", sequenceName = "account_seq", initialValue = 1, allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="account_seq")
	private Long accountId;
	private String name;
	private String email;
	
	
	@Enumerated(EnumType.STRING)
	@Transient
	private AccountType accountType;

}
