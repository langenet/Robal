package ac.project.Robal.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import ac.project.Robal.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

@Entity
public class Owner extends Account {

@OneToMany
private List<Store> stores;

	@Builder
	public Owner (Long accountId,
				  String name,
				  String email,
				  String password,
				  Role role,
				  List<Store> stores) {
		super(accountId,name, email, password, role);
		this.stores = stores;
	}

}
