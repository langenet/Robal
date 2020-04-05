package ac.project.Robal.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Product {

	@Id
	@GeneratedValue
	private Long pid;
	private String productName;
	private String description;
	private Long sku;
}
