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

public class Product {

	@Id
	@GeneratedValue
	private Long productId;
	private String name;
	private String description;
	private Long sku;
}
