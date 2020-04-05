package ac.project.Robal.models;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

public class Owner {

	@OneToOne(cascade = CascadeType.ALL)
	private int storeId;
}
