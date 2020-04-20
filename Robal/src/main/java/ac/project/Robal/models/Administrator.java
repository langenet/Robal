package ac.project.Robal.models;

import javax.persistence.Entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import ac.project.Robal.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@OnDelete(action = OnDeleteAction.CASCADE)
@Entity
public class Administrator extends Account{
	

}
