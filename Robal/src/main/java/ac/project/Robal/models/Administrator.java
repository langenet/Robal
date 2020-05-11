package ac.project.Robal.models;

import javax.persistence.Entity;

import ac.project.Robal.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter

@Entity
public class Administrator extends Account{
	
	@Builder
	public Administrator(Long accountId,
			String name,
			String email,
			String password,
			Role role) {
		super(accountId, name, email, password, role);
	}

}
