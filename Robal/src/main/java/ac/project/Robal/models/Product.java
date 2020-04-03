package ac.project.Robal.models;

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
	private int pid;
	private String productName;
	private int qty;
	private double price;
}
