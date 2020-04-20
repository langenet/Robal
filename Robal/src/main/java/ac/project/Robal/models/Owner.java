package ac.project.Robal.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class Owner extends Account {

@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
private List<Store> stores;

}
