package ac.project.Robal.models;

import javax.persistence.Entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import ac.project.Robal.enums.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
@ToString

@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class Owner extends Account {

//	
//	private OwnerType ownerType;
//	
}
