package ac.project.Robal.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Store_Products {
	@Id
	@GeneratedValue
	private Long sPid;
	private Long sId;
	private Double price;
	private Long quantity;
	
}
